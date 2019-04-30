package com.maxxton.microdocs.crawler.spring.parser;


import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class YamlParser {

    public static Optional<Object> extractParameter(InputStream inputStream, String[] path) {
        if (path.length == 0) {
            return Optional.empty();
        }
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(inputStream);
        if (map == null) {
            return Optional.empty();
        }
        return decentInMap(map, path, 0);
    }

    private static Optional<Object> decentInMap(Object obj, String[] path, int index) {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map) obj;
            if (index == path.length - 1) {
                // There should be a element in that list
                if (map.containsKey(path[index]))
                    return Optional.of(map.get(path[index]));
                else
                    // The element was not present at expected position
                    return Optional.empty();
            } else
                return decentInMap(map.get(path[index]), path, index + 1);
        } else if (obj instanceof List) {
            try {
                int arrayIndex = Integer.parseInt(path[index]);
                return decentInMap(((List) obj).get(arrayIndex), path, index + 1);
            } catch (Exception ignored) {
                return Optional.empty();
            }
        } else {
            if (index == path.length - 1) // Found last element
                return Optional.of(obj);
            else
                return Optional.empty(); // Path ended before element was found
        }
    }

    public static String[] removeSpringContextSymbolAndSplitByDot(String inputYamlPath) {
        String defaultYamlPath = inputYamlPath;
        defaultYamlPath = defaultYamlPath.replace("$", "").replace("{", "").replace("}", "");
        return defaultYamlPath.split("\\.");
    }

    public static String[] splitPathByDot(String yamlPath){
        return yamlPath.split("\\.");
    }
}
