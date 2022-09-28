package com.rishabhsoft.bulkmessaging.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class SESConfig {

   @Bean
    public SesClient getSesClient() {
        Region region = Region.AP_SOUTH_1;
        SesClient sesClient = SesClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        return sesClient;
    }
}
