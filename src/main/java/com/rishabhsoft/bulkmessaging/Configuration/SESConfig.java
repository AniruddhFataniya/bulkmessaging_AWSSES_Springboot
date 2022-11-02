package com.rishabhsoft.bulkmessaging.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class SESConfig {

    Region region = Region.AP_SOUTH_1;
   @Bean
    public SesClient getSesClient() {

        SesClient sesClient = SesClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        return sesClient;

    }

    @Bean
    public CloudWatchClient cloudWatchClient(){
        CloudWatchClient client = CloudWatchClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        return  client;
    }



}
