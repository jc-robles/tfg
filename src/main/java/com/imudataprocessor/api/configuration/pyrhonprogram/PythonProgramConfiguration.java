package com.imudataprocessor.api.configuration.pyrhonprogram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class PythonProgramConfiguration {

    @JsonProperty("programConfigurations")
    private List<ProgramConfiguration> programConfigurations;

}
