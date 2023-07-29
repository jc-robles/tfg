package com.imudataprocessor.api.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor(staticName="of")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestGropingConfiguration {

    @JsonProperty("name")
    private List<GroupingItemConfiguration> select;

}