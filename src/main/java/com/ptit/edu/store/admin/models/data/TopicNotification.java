package com.ptit.edu.store.admin.models.data;

import java.util.Map;

public class TopicNotification {
    private static final String TOPIC_PREFIX = "/topics/";

    private String to;
    private Map<String, String> data;

    public TopicNotification(String topicName, Map<String, String> data) {
        this.to = TOPIC_PREFIX + topicName;
        this.data = data;
    }

    public TopicNotification() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
