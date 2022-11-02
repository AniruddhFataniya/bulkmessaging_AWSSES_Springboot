package com.rishabhsoft.bulkmessaging.controller;

import com.rishabhsoft.bulkmessaging.service.CloudWatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Metric;

import java.util.List;

@RestController
@RequestMapping(value = "/metrics")
public class CloudWatchController {

    @Autowired
    private CloudWatchService cloudWatchService;

    Region region = Region.AP_SOUTH_1;
    CloudWatchClient client = CloudWatchClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();


    @GetMapping
    public List<String> getMetrics(){
        return cloudWatchService.getMetrics(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/sentEmailCount")
    public String getSentEmailCount(){
        return cloudWatchService.getSentEmailCount(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/deliveredEmailCount")
    public String getDeliveredEmailCount(){
        return cloudWatchService.getDeliveredEmailCount(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/openedEmailCount")
    public String getOpenedEmailCount(){
        return cloudWatchService.getOpenedEmailCount(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/clickCount")
    public String getClickCount(){
        return cloudWatchService.getClickCount(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/bounceCount")
    public String getBounceCount(){
        return cloudWatchService.getBounceCount(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/spamMarkCount")
    public String getSpamMarkCount(){
        return cloudWatchService.getSpamMarkCount(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/unsubscribeCount")
    public String getUnsubscribeCount(){
        return cloudWatchService.getUnsubscribeCount(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/renderingFailureCount")
    public String getRenderingFailureCount(){
        return cloudWatchService.getRenderingFailureCount(client);
    }
}
