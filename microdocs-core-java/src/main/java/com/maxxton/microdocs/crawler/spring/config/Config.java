package com.maxxton.microdocs.crawler.spring.config;

public class Config {

    private String eventProducerType;
    private String eventPublisherMethodCall;
    private String pathToProjectSrcFolder;
    private String pathToApplicationYamlFile;
    private String pathToEventConsumerQueueName;
    private String pathToEventConsumerBindingKeys;
    private String addedPrefixToEventConsumerQueueName;

    public String getAddedPrefixToEventConsumerQueueName() {
        return addedPrefixToEventConsumerQueueName;
    }

    public void setAddedPrefixToEventConsumerQueueName(String addedPrefixToEventConsumerQueueName) {
        this.addedPrefixToEventConsumerQueueName = addedPrefixToEventConsumerQueueName;
    }

    public String getEventProducerType() {
        return eventProducerType;
    }

    public void setEventProducerType(String eventProducerType) {
        this.eventProducerType = eventProducerType;
    }

    public String getEventPublisherMethodCall() {
        return eventPublisherMethodCall;
    }

    public void setEventPublisherMethodCall(String eventPublisherMethodCall) {
        this.eventPublisherMethodCall = eventPublisherMethodCall;
    }

    public String getPathToProjectSrcFolder() {
        return pathToProjectSrcFolder;
    }

    public void setPathToProjectSrcFolder(String pathToProjectSrcFolder) {
        this.pathToProjectSrcFolder = pathToProjectSrcFolder;
    }

    public String getPathToApplicationYamlFile() {
        return pathToApplicationYamlFile;
    }

    public void setPathToApplicationYamlFile(String pathToApplicationYamlFile) {
        this.pathToApplicationYamlFile = pathToApplicationYamlFile;
    }

    public String getPathToEventConsumerQueueName() {
        return pathToEventConsumerQueueName;
    }

    public void setPathToEventConsumerQueueName(String pathToEventConsumerQueueName) {
        this.pathToEventConsumerQueueName = pathToEventConsumerQueueName;
    }

    public String getPathToEventConsumerBindingKeys() {
        return pathToEventConsumerBindingKeys;
    }

    public void setPathToEventConsumerBindingKeys(String pathToEventConsumerBindingKeys) {
        this.pathToEventConsumerBindingKeys = pathToEventConsumerBindingKeys;
    }
}
