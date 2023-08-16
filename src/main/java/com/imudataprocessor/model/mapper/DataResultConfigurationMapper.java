package com.imudataprocessor.model.mapper;

import com.imudataprocessor.api.configuration.pyrhonprogram.DataResultConfiguration;
import com.imudataprocessor.api.dto.out.createtest.FieldDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataResultConfigurationMapper {

    @Named("DataResultConfigurationFromFieldDTO")
    @Mapping(target = "nameField", source = "name")
    @Mapping(target = "dataType", source = "dataType")
    @Mapping(target = "graph", source = "graph")
    DataResultConfiguration map(FieldDTO fieldDTO);

    List<DataResultConfiguration> map(List<FieldDTO> fieldDTOS);

}
