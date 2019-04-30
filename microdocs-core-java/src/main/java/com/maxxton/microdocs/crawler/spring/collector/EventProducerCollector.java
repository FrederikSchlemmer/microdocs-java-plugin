package com.maxxton.microdocs.crawler.spring.collector;

import com.maxxton.microdocs.core.reflect.ClassType;
import com.maxxton.microdocs.core.reflect.ReflectClass;
import com.maxxton.microdocs.crawler.Crawler;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.*;
import java.io.File;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;

public class EventProducerCollector {

    public static class CodeAnalyzerTreeVisitor extends TreePathScanner<Object, Trees> {

        ReflectClass searchedClass;

        public void setSearchedClass(ReflectClass searchedClass) {
            this.searchedClass = searchedClass;
        }

        private void extractEvents(MethodTree methodTree) {
            List<? extends StatementTree> methodStatements = methodTree.getBody().getStatements();
            String eventObject = "";

            for (StatementTree statement : methodStatements) {
                String statementAsString = statement.toString();
                if (statementAsString.contains(Crawler.configuration.getEventPublisherMethodCall())) {
                    eventObject = statementAsString.substring(statementAsString.indexOf("(") + 1, statementAsString.indexOf(")"));
                }
            }

            for (StatementTree statement : methodStatements) {
                String statementAsString = statement.toString();
                if (statementAsString.contains("= new " + Crawler.configuration.getEventProducerType()) && checkIfEventObjectIsDefinedInStatement(statementAsString, eventObject)) {
                    searchedClass.getEventProducers().add(statementAsString.substring(
                            statementAsString.indexOf("(") + 2,
                            statementAsString.indexOf(")") - 1));
                }
            }
        }

        @Override
        public Object visitMethod(MethodTree methodTree, Trees trees) {
            String typeNameQualified = getEnclosingClassNameIfAvailable(trees);

            // Skip or bad stuff happens
            if (typeNameQualified == null) {
                return super.visitMethod(methodTree, trees);
            }

            if (typeNameQualified.equals(searchedClass.getName()))
                if (searchedClass.getType().equals(ClassType.CLASS)) {
                    this.extractEvents(methodTree);
                }

            return super.visitMethod(methodTree, trees);
        }

        private boolean checkIfEventObjectIsDefinedInStatement(String statementAsString, String eventObject) {
            for (String word : statementAsString.split(" ")) {
                if (word.trim().equals(eventObject)) {
                    return true;
                }
            }
            return false;
        }

        private String getEnclosingClassNameIfAvailable(Trees trees) {
            // Method element is enclosed by its class, I suppose
            try {
                TypeElement typeElement = (TypeElement) trees.getElement(getCurrentPath()).getEnclosingElement();
                return typeElement.getQualifiedName().toString();
            } catch (NullPointerException e) {
                // getElement will return null for an inner anonymous class
                return null;
            }
        }

    }

    /**
     * Custom annotation processor to run our visitor
     */
    @SupportedSourceVersion(SourceVersion.RELEASE_8)
    @SupportedAnnotationTypes("*")
    public static class CodeAnalyzerProcessor extends AbstractProcessor {

        private final CodeAnalyzerTreeVisitor visitor = new CodeAnalyzerTreeVisitor();
        private Trees trees;

        @Override
        public void init(ProcessingEnvironment pe) {
            super.init(pe);
            trees = Trees.instance(pe);
        }

        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
            for (Element e : roundEnvironment.getRootElements()) {
                // Normally the ellement should represent a class
                TreePath tp = trees.getPath(e);
                // invoke the scanner
                visitor.scan(tp, trees);
            }
            return true;    // handled, don't invoke other processors
        }
    }

    public static void runEventExtraction(String filePath, ReflectClass searchedClass) throws Exception {


        File searchDir = new File(filePath);
        if (!searchDir.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory: " + searchDir.getAbsolutePath());
        }

        //Get an instance of java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        //Get a new instance of the standard file manager implementation
        StandardJavaFileManager fileManager = compiler.
                getStandardFileManager(null, null, null);

        // Get the list of java file objects, in this case we have only
        // one file, TestClass.java

        fileManager.setLocation(StandardLocation.SOURCE_PATH, singleton(searchDir.getAbsoluteFile()));
        Iterable<JavaFileObject> sources = fileManager.list(
                StandardLocation.SOURCE_PATH //locationFor("src/test/java")
                , ""
                , singleton(JavaFileObject.Kind.SOURCE)
                , true);

        // Create the compilation task
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, sources);

        // Set the annotation processor to the compiler task
        CodeAnalyzerProcessor codeAnalyzerProcessor = new CodeAnalyzerProcessor();
        codeAnalyzerProcessor.visitor.setSearchedClass(searchedClass);
        task.setProcessors(singleton(codeAnalyzerProcessor));

        task.call();
    }
}
