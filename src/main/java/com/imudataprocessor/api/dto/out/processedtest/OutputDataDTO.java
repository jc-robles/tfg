package com.imudataprocessor.api.dto.out.processedtest;

import lombok.Data;

import java.util.List;

@Data
public class OutputDataDTO {

    private List<OutputAlphanumericDataDTO> alphanumericDataList;

    private List<OutputArrayDataDTO> arrayDataList;

}
