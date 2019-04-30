package com.maxxton.microdocs.crawler.spring.collector;

import com.maxxton.microdocs.core.reflect.ReflectClass;
import com.maxxton.microdocs.crawler.Crawler;
import com.maxxton.microdocs.crawler.spring.parser.YamlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

public class EventConsumerCollector {

    public void runExtraction(ReflectClass searchedClass) {

        String searchedClassName = searchedClass.getSimpleName().toLowerCase();

        if (checkIfSearchedClassMatchesQueueName(searchedClassName)) {
            String[] splittedPathToEventConsumerBindingKeys = YamlParser.splitPathByDot(Crawler.configuration.getPathToEventConsumerBindingKeys());
            Optional<Object> extractedBindingKeys = YamlParser.extractParameter(this.readApplicationYaml(Crawler.configuration.getPathToApplicationYamlFile()), splittedPathToEventConsumerBindingKeys);
            if (extractedBindingKeys.isPresent()) {
                ArrayList<String> extractedEventConsumers = (ArrayList)extractedBindingKeys.get();
                searchedClass.getEventConsumers().addAll(extractedEventConsumers);
            }
        }
    }

    private boolean checkIfSearchedClassMatchesQueueName(String searchedClassName) {
        return searchedClassName.equals(extractAndConvertQueueName(this.readApplicationYaml(Crawler.configuration.getPathToApplicationYamlFile())));
    }

    private String extractAndConvertQueueName(InputStream input) {
        String[] splittedPathToEventConsumerQueueName = YamlParser.splitPathByDot(Crawler.configuration.getPathToEventConsumerQueueName());
        Optional<Object> extractedQueueName = YamlParser.extractParameter(input, splittedPathToEventConsumerQueueName);
        if (extractedQueueName.isPresent()) {
            return extractedQueueName.get().toString().toLowerCase().replace(Crawler.configuration.getAddedPrefixToEventConsumerQueueName().toLowerCase(), "");
        }
        return null;
    }

    private InputStream readApplicationYaml(String applicationYamlPath) {
        try {
            return new FileInputStream(new File(applicationYamlPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
