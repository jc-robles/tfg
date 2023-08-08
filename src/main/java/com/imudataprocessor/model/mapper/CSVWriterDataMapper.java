package com.imudataprocessor.model.mapper;

import com.imudataprocessor.api.dto.internal.InternalDataDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class CSVWriterDataMapper {

    public List<String[]> map(final InternalDataDTO dataObtained) {
        final List<String[]> data = new ArrayList<>();
        IntStream.rangeClosed(0, dataObtained.getTimestamp().size() - 1).forEach(value ->
                data.add(new String[]{
                        dataObtained.getTimestamp().get(value),
                        dataObtained.getAccelerometerX().get(value),
                        dataObtained.getAccelerometerY().get(value),
                        dataObtained.getAccelerometerZ().get(value),
                        dataObtained.getGyroscopeX().get(value),
                        dataObtained.getGyroscopeY().get(value),
                        dataObtained.getGyroscopeZ().get(value),
                        dataObtained.getQuaternionW().get(value),
                        dataObtained.getQuaternionX().get(value),
                        dataObtained.getQuaternionY().get(value),
                        dataObtained.getQuaternionZ().get(value)})
        );
        return data;
    }

}
