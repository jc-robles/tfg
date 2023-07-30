package com.imudataprocessor.api.controller.createtest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateTestDTO {

    @JsonProperty("name_test")
    private String nameTest;

    @JsonProperty("fields")
    private List<FieldDTO> fields;

}
