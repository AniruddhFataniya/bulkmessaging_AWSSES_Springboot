package com.rishabhsoft.bulkmessaging.service;

import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

import java.util.List;


public interface CloudWatchService {

    public List<String> getMetrics(CloudWatchClient cloudWatchClient);

    public String getSentEmailCount(CloudWatchClient cloudWatchClient);

    public String getDeliveredEmailCount(CloudWatchClient cloudWatchClient);

    public String getOpenedEmailCount(CloudWatchClient cloudWatchClient);

    public String getClickCount(CloudWatchClient cloudWatchClient);

    public String getBounceCount(CloudWatchClient cloudWatchClient);

    public String getSpamMarkCount(CloudWatchClient cloudWatchClient);

    public String getUnsubscribeCount(CloudWatchClient cloudWatchClient);

    public String getRenderingFailureCount(CloudWatchClient cloudWatchClient);



}
