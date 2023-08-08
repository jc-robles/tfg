package com.imudataprocessor.model.mapper;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.dto.out.createtest.CreateTestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DataResultConfigurationMapper.class})
public interface ProgramConfigurationMapper {

    @Mapping(target = "nameTest", source = "nameTest")
    @Mapping(target = "dataResult", source = "fields", qualifiedByName = "DataResultConfigurationFromFieldDTO")
    ProgramConfiguration map(final CreateTestDTO createTestDTO);

}
