package com.rishabhsoft.bulkmessaging.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BulkEmailWithTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, message = "Please enter the email address")
    @NotNull(message = "Please enter the email address")
    @Email(message = "Please enter a valid email address")
    private String sender;

    @Size(min = 1, message = "Please enter at least one recipient")
    @NotNull(message = "Please enter at least one recipient")
    @Email(message = "Please enter valid email address")
    private String toAddresses;

    @NotNull(message = "Please enter a template name")
    @Size(min = 1, message = "Please enter a template name")
    private String templateName;

    @NotNull(message = "required")
    @Size(min = 1, message = "required")
    private String defaultTemplateData;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String name;

}
