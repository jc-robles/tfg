package com.imudataprocessor.model.mapper;

import com.imudataprocessor.api.controller.DataDTO;
import com.imudataprocessor.api.service.InternalDataDTO;
import org.mapstruct.Mapper;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface DataDTOMapper {

    DataDTO map(final InternalDataDTO dataDTO);

    default Float map(final String s) {
        if (Objects.isNull(s)) {
            return null;
        }
        try {
            return Float.valueOf(s.replace(",", "."));
        } catch (final NumberFormatException e) {
            return null;
        }
    }

}
