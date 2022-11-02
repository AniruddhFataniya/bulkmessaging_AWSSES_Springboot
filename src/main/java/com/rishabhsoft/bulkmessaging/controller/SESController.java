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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.ses.SesClient;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "/emails")
public class SESController {

    // configuring region and SES client
    Region region = Region.AP_SOUTH_1;
    SesClient sesClient = SesClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

   // private SesClient sesClient;
    @Autowired
    private SESService service;

    // Getting the list of verified identities
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/identities")
    public List<String> getIdentities() throws IOException {
       // System.out.println(">>>>>>>>Client: "+sesClient);
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
    ResponseEntity<BulkEmailWithTemplate> sendBulkEmailWithTemplate(@Valid @RequestBody BulkEmailWithTemplate bulkEmailWithTemplate, BindingResult theBindingResult ){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        service.sendBulkEmail(sesClient,bulkEmailWithTemplate);
        service.saveBulkEmail(bulkEmailWithTemplate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // creating a template
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/template")
    ResponseEntity<EmailTemplate> createTemplate(@Valid @RequestBody EmailTemplate template, Model model){
        service.createTemplate(sesClient,template);
        service.saveTemplate(template);
        model.addAttribute("template", template);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // deleting a template
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/template/{templateName}")
    ResponseEntity<EmailTemplate> deleteTemplate(@PathVariable String templateName){
        service.deleteTemplateFromDb(templateName);
        service.deleteTemplate(sesClient,templateName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // getting the list of the templates from SES
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/listTemplate")
    public List<String> listTemplate(){
        return service.listTemplate(sesClient);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/listTemplatesWithData")
    public List<EmailTemplate> listTemplateFromDb(){
        return service.listTemplateFromDb();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/searchTemplate/{templateName}")
    public Boolean searchTemplate(@PathVariable String templateName){
        return service.searchTemplate(sesClient,templateName);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/emailHistory")
    public List<BulkEmailWithTemplate> getEmailHistory(){
        return service.getEmailHistory();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/searchIdentity/{identity}")
    public Boolean searchIdentity(@PathVariable String identity) throws IOException {
        return service.searchIdentity(identity, sesClient);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/createIdentity")
    public ResponseEntity<String> createIdentity(@RequestBody String identity){
        service.createIdentity(sesClient,identity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/deleteIdentity/{identity}")
    public ResponseEntity<String> deleteIdentity(@PathVariable String identity){
        service.deleteIdentity(sesClient,identity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/clearEmailHistory")
    public ResponseEntity<String> clearEmailHistory(){
        service.clearEmailHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/templateCount")
    public Long templateCount(){
        return service.templateCount();
    }
}
