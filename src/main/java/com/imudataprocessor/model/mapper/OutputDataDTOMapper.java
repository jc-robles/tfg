package com.imudataprocessor.model.mapper;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.dto.out.processedtest.DataTypeEnum;
import com.imudataprocessor.api.dto.out.processedtest.OutputAlphanumericDataDTO;
import com.imudataprocessor.api.dto.out.processedtest.OutputArrayDataDTO;
import com.imudataprocessor.api.dto.out.processedtest.OutputDataDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OutputDataDTOMapper {

    public OutputDataDTO map(final Optional<ProgramConfiguration> programConfiguration, final Map<String, Object> data) {
        return programConfiguration.map(programConfiguration1 -> {
            final OutputDataDTO dataDTO2 = new OutputDataDTO();
            dataDTO2.setAlphanumericDataList(this.getAllAlphanumericDataList(programConfiguration1, data));
            dataDTO2.setArrayDataList(this.getAllArrayDataList(programConfiguration1, data));
            return dataDTO2;
        }).orElse(new OutputDataDTO());
    }

    private List<OutputAlphanumericDataDTO> getAllAlphanumericDataList(final ProgramConfiguration programConfiguration, final Map<String, Object> data) {
        return programConfiguration.getDataResult().stream()
                .filter(dataResultConfiguration1 -> ObjectUtils.nullSafeEquals(dataResultConfiguration1.getDataType(), DataTypeEnum.ALPHANUMERIC.name()))
                .map(dataResultConfiguration -> {
                    final Object value = data.get(dataResultConfiguration.getNameField());
                    final OutputAlphanumericDataDTO dataDTO1 = new OutputAlphanumericDataDTO();
                    dataDTO1.setName(dataResultConfiguration.getNameField());
                    dataDTO1.setValue(String.valueOf(value));
                    return dataDTO1;
                }).toList();
    }

    private List<OutputArrayDataDTO> getAllArrayDataList(final ProgramConfiguration programConfiguration, final Map<String, Object> data) {
        return programConfiguration.getDataResult().stream()
                .filter(dataResultConfiguration1 -> ObjectUtils.nullSafeEquals(dataResultConfiguration1.getDataType(), DataTypeEnum.DATA_ARRAY.name()))
                .map(dataResultConfiguration -> {
                    final List<Double> dataList = (List<Double>) data.get(dataResultConfiguration.getNameField());
                    final OutputArrayDataDTO dataDTO1 = new OutputArrayDataDTO();
                    dataDTO1.setName(dataResultConfiguration.getNameField());
                    dataDTO1.setValue(dataList.stream().map(Double::floatValue).toList());
                    dataDTO1.setGroup(dataResultConfiguration.getGroupData());
                    return dataDTO1;
                }).toList();
    }

}
