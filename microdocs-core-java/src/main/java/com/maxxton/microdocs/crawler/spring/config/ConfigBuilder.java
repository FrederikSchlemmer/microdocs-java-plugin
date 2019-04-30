package com.maxxton.microdocs.crawler.spring.config;

public class ConfigBuilder {

    private Config config;

    public ConfigBuilder() {
        this.config = new Config();
    }

    public ConfigBuilder addedPrefixToEventConsumerQueueName(String addedPrefixToEventConsumerQueueName) {
        this.config.setAddedPrefixToEventConsumerQueueName(addedPrefixToEventConsumerQueueName);
        return this;
    }

    public ConfigBuilder eventProducerType(String eventProducerType) {
        this.config.setEventProducerType(eventProducerType);
        return this;
    }

    public ConfigBuilder eventPublisherMethodCall(String eventPublisherMethodCall) {
        this.config.setEventPublisherMethodCall(eventPublisherMethodCall);
        return this;
    }

    public ConfigBuilder pathToProjectSrcFolder(String pathToProjectSrcFolder) {
        this.config.setPathToProjectSrcFolder(pathToProjectSrcFolder);
        return this;
    }

    public ConfigBuilder pathToApplicationYamlFile(String pathToApplicationYamlFile) {
        this.config.setPathToApplicationYamlFile(pathToApplicationYamlFile);
        return this;
    }

    public ConfigBuilder pathToEventConsumerQueueName(String pathToEventConsumerQueueName) {
        this.config.setPathToEventConsumerQueueName(pathToEventConsumerQueueName);
        return this;
    }

    public ConfigBuilder pathToEventConsumerBindingKeys(String pathToEventConsumerBindingKeys) {
        this.config.setPathToEventConsumerBindingKeys(pathToEventConsumerBindingKeys);
        return this;
    }

    public Config build() {
        return this.config;
    }
}
