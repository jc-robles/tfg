package com.imudataprocessor.api.dto.out.processedtest;

import lombok.Data;

import java.util.List;

@Data
public class OutputArrayDataDTO {

    private String name;

    private List<Float> value;

    private String graph;

}
