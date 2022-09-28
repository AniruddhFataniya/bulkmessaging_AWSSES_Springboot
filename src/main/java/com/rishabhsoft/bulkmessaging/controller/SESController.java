package com.rishabhsoft.bulkmessaging.controller;

import com.rishabhsoft.bulkmessaging.model.BulkEmailWithTemplate;
import com.rishabhsoft.bulkmessaging.model.Email;
import com.rishabhsoft.bulkmessaging.model.EmailTemplate;
import com.rishabhsoft.bulkmessaging.model.EmailWithTemplate;
import com.rishabhsoft.bulkmessaging.service.SESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.ListIdentitiesResponse;
import software.amazon.awssdk.services.ses.model.SesException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/emails")
public class SESController {

    Region region = Region.AP_SOUTH_1;
    SesClient sesClient = SesClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();
    @Autowired
    private SESService service;

    @GetMapping("/identities")
    public List<String> getIdentities() throws IOException {
        System.out.println(">>>>>>>>Client: "+sesClient);
        return service.getIdentity(sesClient);
    }

    @PostMapping("/simpleEmail")
    ResponseEntity<Email> sendEmail(@RequestBody Email email) throws MessagingException {
        service.sendEmail(sesClient,email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/emailWithTemplate")
    ResponseEntity<EmailWithTemplate> sendEmailWithTemplate(@RequestBody EmailWithTemplate emailWithTemplate) throws MessagingException{
        service.sendEmailWithTemplate(sesClient,emailWithTemplate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/bulkEmailWithTemplate")
    ResponseEntity<BulkEmailWithTemplate> sendBulkEmailWithTemplate(@RequestBody BulkEmailWithTemplate bulkEmailWithTemplate ){
        service.sendBulkEmail(sesClient,bulkEmailWithTemplate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/template")
    ResponseEntity<EmailTemplate> createTemplate(@RequestBody EmailTemplate template){
        service.createTemplate(sesClient,template);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/template/{templateName}")
    ResponseEntity<EmailTemplate> deleteTemplate(@PathVariable String templateName){
        service.deleteTemplate(sesClient,templateName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
