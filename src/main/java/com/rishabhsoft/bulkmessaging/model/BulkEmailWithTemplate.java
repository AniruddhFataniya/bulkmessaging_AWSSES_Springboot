package com.rishabhsoft.bulkmessaging.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkEmailWithTemplate {

    private Long id;

    private String sender;

    //@JsonProperty("collection")
    private List<String> toAddresses;
    //@JsonProperty("collection")
    private List<String> ccAddresses;
    //@JsonProperty("collection")
    private List<String> bccAddresses;

    private String templateName;

    private String defaultTemplateData;
}
