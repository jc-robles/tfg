package com.imudataprocessor.api.service;

import lombok.Data;

import java.util.List;

@Data
public class OutputArrayDataDTO {

    private String name;

    private List<String> value;

    private String group;

}
