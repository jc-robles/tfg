package com.imudataprocessor.model.mapper;

import com.imudataprocessor.api.dto.internal.InternalDataDTO;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Component
public class InternalDataDTOMapper {

    @Value("${separator}")
    private char separator;

    public InternalDataDTO map(final File file) {
        final InternalDataDTO internalDataDTO = new InternalDataDTO();
        try (final CSVReader csvReader = new CSVReaderBuilder(new FileReader(file)).withCSVParser(new CSVParserBuilder().withSeparator(this.separator).build()).build()) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                if (values.length > 1) {
                    internalDataDTO.addTimestamp(values[0]);
                    internalDataDTO.addAccelerometerX(values[1]);
                    internalDataDTO.addAccelerometerY(values[2]);
                    internalDataDTO.addAccelerometerZ(values[3]);
                    internalDataDTO.addGyroscopeX(values[4]);
                    internalDataDTO.addGyroscopeY(values[5]);
                    internalDataDTO.addGyroscopeZ(values[6]);
                    internalDataDTO.addQuaternionW(values[7]);
                    internalDataDTO.addQuaternionX(values[8]);
                    internalDataDTO.addQuaternionY(values[9]);
                    internalDataDTO.addQuaternionZ(values[10]);
                }
            }
        } catch (final IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return internalDataDTO;
    }

}
