package com.imudataprocessor.api.configuration.pyrhonprogram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class ProgramConfiguration {

    @JsonProperty("nameFile")
    private String nameFile;

    @JsonProperty("nameTest")
    private String nameTest;

    @JsonProperty("dataResult")
    private List<DataResultConfiguration> dataResult;

}
