package com.imudataprocessor.api.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public class GraphItemConfiguration {

    @JsonProperty("id")
    @NonNull
    private String id;

    @JsonProperty("value")
    @NonNull
    private String value;

    @JsonProperty("name")
    @NonNull
    private String name;

}
