package com.imudataprocessor.api.controller.createtest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FieldDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("data_type")
    private String dataType;

    @JsonProperty("grouping")
    private String grouping;

}
