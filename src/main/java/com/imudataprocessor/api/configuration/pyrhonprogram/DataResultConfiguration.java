package com.imudataprocessor.api.configuration.pyrhonprogram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DataResultConfiguration {

    @JsonProperty("nameField")
    private String nameField;

    @JsonProperty("dataType")
    private String dataType;

    @JsonProperty("graph")
    private String graph;

}
