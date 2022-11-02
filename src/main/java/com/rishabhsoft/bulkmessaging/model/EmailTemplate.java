package com.rishabhsoft.bulkmessaging.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Please enter the template name")
    @Size(min = 1, message = "Please enter the template name")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Only alphanumeric characters, '_', and '-' are allowed")
    private String templateName;

    @NotNull(message = "Please enter the subject")
    @Size(min = 1, message = "Please enter the subject")
    private String subjectPart;

    @Lob
    private String htmlPart;

    @NotNull(message = "Please enter the alternative text")
    @Size(min = 1, message = "Please enter the alternative text")
    private String textPart;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

}
