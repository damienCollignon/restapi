package org.collignon.restapi.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record User(@Id String id, @NotBlank String firstName, @NotBlank String lastName){}
