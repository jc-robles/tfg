package com.imudataprocessor.api.dto.out.createtest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FieldDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("data_type")
    private String dataType;

    @JsonProperty("graph")
    private String graph;

}
