package com.rishabhsoft.bulkmessaging.model;

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

    private String sender;

    private List<String> receiver;

    private String templateName;

    private String defaultTemplateData;
}
