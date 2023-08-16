package com.imudataprocessor.api.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestGraphConfiguration {

    @JsonProperty("name")
    private List<GraphItemConfiguration> select;

}