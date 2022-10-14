package com.rishabhsoft.bulkmessaging.controller;

import com.rishabhsoft.bulkmessaging.model.BulkEmailWithTemplate;
import com.rishabhsoft.bulkmessaging.model.Email;
import com.rishabhsoft.bulkmessaging.model.EmailTemplate;
import com.rishabhsoft.bulkmessaging.model.EmailWithTemplate;
import com.rishabhsoft.bulkmessaging.service.SESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/emails")
public class SESController {

    // configuring region and SES client
    Region region = Region.AP_SOUTH_1;
    SesClient sesClient = SesClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();
    @Autowired
    private SESService service;

    // Getting the list of verified identities
    @GetMapping("/identities")
    public List<String> getIdentities() throws IOException {
        System.out.println(">>>>>>>>Client: "+sesClient);
        return service.getIdentity(sesClient);
    }

    // sending a simple email to a single email id
    @PostMapping("/simpleEmail")
    ResponseEntity<Email> sendEmail(@RequestBody Email email) throws MessagingException {
        service.sendEmail(sesClient,email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // sending an email with template to a single email id
    @PostMapping("/emailWithTemplate")
    ResponseEntity<EmailWithTemplate> sendEmailWithTemplate(@RequestBody EmailWithTemplate emailWithTemplate) throws MessagingException{
        service.sendEmailWithTemplate(sesClient,emailWithTemplate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // sending bulk emails with a template
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/bulkEmailWithTemplate")
    ResponseEntity<BulkEmailWithTemplate> sendBulkEmailWithTemplate(@RequestBody BulkEmailWithTemplate bulkEmailWithTemplate ){
        service.sendBulkEmail(sesClient,bulkEmailWithTemplate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // creating a template
    @PostMapping("/template")
    ResponseEntity<EmailTemplate> createTemplate(@RequestBody EmailTemplate template, Model model){
        service.createTemplate(sesClient,template);
        model.addAttribute("template", template);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // deleting a template
    @DeleteMapping("/template/{templateName}")
    ResponseEntity<EmailTemplate> deleteTemplate(@PathVariable String templateName){
        service.deleteTemplate(sesClient,templateName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // getting the list of the templates from SES
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/listTemplate")
    public List<String> listTemplate(){
        return service.listTemplate(sesClient);
    }


}
