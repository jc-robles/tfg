package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.controller.createtest.DataTypeEnum;
import com.imudataprocessor.api.service.*;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private JsonService jsonService;

    @Value("${test_extension}")
    private String testExtension;

    @Value("${main-file-path}")
    private String mainFilePath;

    @Value("${tests-not-processed-path}")
    private String testsNotProcessedPath;

    @Value("${split-tests-not-processed-path}")
    private String splitTestsNotProcessedPath;

    @Value("${tests-processed-path}")
    private String testsProcessedPath;

    @Value("${origin-file-path}")
    private String originFilePath;

    @Override
    public void save(final String path, final String fileName, final byte[] bytes) throws IOException {
        final File file = this.createFile(path, fileName);
        try (final OutputStream os = new FileOutputStream(file)) {
            os.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File split(final String fileName, final Integer start, final Integer end) throws IOException {
        final InternalDataDTO dataObtained =  this.obtainCompleteDataToFile(this.mainFilePath);
        final InternalDataDTO splitData =  this.splitData(dataObtained, start, end);
        return this.saveToCsv(splitData, fileName);
    }


    @Override
    public InternalDataDTO getDataFromMainFile() throws IOException {
        return this.obtainDataToFile(this.mainFilePath);
    }

    @Override
    public InternalDataDTO getDataFromTest(final String nameTest) throws IOException {
        return this.obtainDataToFile(this.splitTestsNotProcessedPath + "/" + nameTest + this.testExtension);
    }

    @Override
    public InternalDataDTO getDataFromProcessedTest(final String nameTest) throws IOException {
        return this.obtainDataToFile(this.testsProcessedPath + "/" + nameTest + this.testExtension);
    }

    @Override
    public void deleteTest(final String nameTest) throws IOException {
        Files.deleteIfExists(Paths.get(this.splitTestsNotProcessedPath + "/" + nameTest + this.testExtension));
    }

    @Override
    public void deleteAllTest() throws IOException  {
        Files.walk(Paths.get(originFilePath)).forEach(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
            }
        });
    }

    private InternalDataDTO obtainDataToFile(final @NonNull String filePath) throws IOException {
        final InternalDataDTO dataDTO = obtainCompleteDataToFile(filePath);
        this.deleteHeaders(dataDTO);
        return dataDTO;
    }

    public OutputDataDTO obtainDataToFileProcessed(final Optional<ProgramConfiguration> programConfiguration, final @NonNull String filePath) throws IOException {
        Map<String, Object> map = (Map<String, Object>) jsonService.readFile(testsProcessedPath + "/" + filePath + ".json", Map.class);
        return programConfiguration.map(programConfiguration1 -> {
            List<OutputAlphanumericDataDTO> dataResultConfigurationAlphanumeric = programConfiguration1.getDataResult().stream()
                    .filter(dataResultConfiguration1 -> ObjectUtils.nullSafeEquals(dataResultConfiguration1.getDataType(), DataTypeEnum.ALPHANUMERIC.name()))
                    .map(dataResultConfiguration -> {
                        final Object o = map.get(dataResultConfiguration.getNameField());
                        final OutputAlphanumericDataDTO dataDTO1 = new OutputAlphanumericDataDTO();
                        dataDTO1.setName(dataResultConfiguration.getNameField());
                        dataDTO1.setValue(String.valueOf(o));
                        return dataDTO1;
                    }).toList();
            List<OutputArrayDataDTO> dataResultConfigurationDataArray = programConfiguration1.getDataResult().stream()
                    .filter(dataResultConfiguration1 -> ObjectUtils.nullSafeEquals(dataResultConfiguration1.getDataType(), DataTypeEnum.DATA_ARRAY.name()))
                    .map(dataResultConfiguration -> {
                        final List<Double> dataList = (List<Double>) map.get(dataResultConfiguration.getNameField());
                        final OutputArrayDataDTO dataDTO1 = new OutputArrayDataDTO();
                        dataDTO1.setName(dataResultConfiguration.getNameField());
                        dataDTO1.setValue(dataList.stream().map(Double::floatValue).toList());
                        dataDTO1.setGroup(dataResultConfiguration.getGroupData());
                        return dataDTO1;
                    }).toList();

            final OutputDataDTO dataDTO2 = new OutputDataDTO();
            dataDTO2.setAlphanumericDataList(dataResultConfigurationAlphanumeric);
            dataDTO2.setArrayDataList(dataResultConfigurationDataArray);
            return dataDTO2;
        }).orElse(new OutputDataDTO());
    }

    private InternalDataDTO obtainCompleteDataToFile(final @NonNull String filePath) throws IOException {
        final Optional<File> file = Files. walk(Paths.get(filePath)).filter(Files::isRegularFile).map(Path::toFile).findFirst();

        final InternalDataDTO internalDataDTO = new InternalDataDTO();
        if (file.isPresent()) {
            try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file.get())).withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build()) {
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
            } catch (IOException | CsvValidationException e) {
                throw new RuntimeException(e);
            }
        }
        return internalDataDTO;
    }

    private InternalDataDTO splitData(final InternalDataDTO internalDataDTO, final Integer start, final Integer end) {
        final int fileStart = Objects.isNull(start) ? 0 : start;
        final int fileEnd = Objects.isNull(end) ? internalDataDTO.getAccelerometerX().size() : end + 2;
        InternalDataDTO internalDataSplit = new InternalDataDTO();
        internalDataSplit.setTimestamp(internalDataDTO.getTimestamp().subList(fileStart, fileEnd));
        internalDataSplit.setAccelerometerX(internalDataDTO.getAccelerometerX().subList(fileStart, fileEnd));
        internalDataSplit.setAccelerometerY(internalDataDTO.getAccelerometerY().subList(fileStart, fileEnd));
        internalDataSplit.setAccelerometerZ(internalDataDTO.getAccelerometerZ().subList(fileStart, fileEnd));
        internalDataSplit.setGyroscopeX(internalDataDTO.getGyroscopeX().subList(fileStart, fileEnd));
        internalDataSplit.setGyroscopeY(internalDataDTO.getGyroscopeY().subList(fileStart, fileEnd));
        internalDataSplit.setGyroscopeZ(internalDataDTO.getGyroscopeZ().subList(fileStart, fileEnd));
        internalDataSplit.setQuaternionW(internalDataDTO.getQuaternionW().subList(fileStart, fileEnd));
        internalDataSplit.setQuaternionX(internalDataDTO.getQuaternionX().subList(fileStart, fileEnd));
        internalDataSplit.setQuaternionY(internalDataDTO.getQuaternionY().subList(fileStart, fileEnd));
        internalDataSplit.setQuaternionZ(internalDataDTO.getQuaternionZ().subList(fileStart, fileEnd));
        return internalDataSplit;
    }

    private File createFile(final String filePath, final String fileName) throws IOException {
        final Path path = Paths.get(Objects.requireNonNull(filePath));
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        return new File(filePath + "/" + fileName);
    }

    private void deleteHeaders(final InternalDataDTO internalDataDTO) {
        internalDataDTO.getTimestamp().subList(0, 2).clear();
        internalDataDTO.getAccelerometerX().subList(0, 2).clear();
        internalDataDTO.getAccelerometerY().subList(0, 2).clear();
        internalDataDTO.getAccelerometerZ().subList(0, 2).clear();
        internalDataDTO.getGyroscopeX().subList(0, 2).clear();
        internalDataDTO.getGyroscopeY().subList(0, 2).clear();
        internalDataDTO.getGyroscopeZ().subList(0, 2).clear();
        internalDataDTO.getQuaternionW().subList(0, 2).clear();
        internalDataDTO.getQuaternionX().subList(0, 2).clear();
        internalDataDTO.getQuaternionY().subList(0, 2).clear();
        internalDataDTO.getQuaternionZ().subList(0, 2).clear();
    }

    private File saveToCsv(final InternalDataDTO dataObtained, final String fileName) throws IOException {
        final List<String[]> data = new ArrayList<>();
        IntStream.rangeClosed(0, dataObtained.getTimestamp().size() - 1).forEach(value ->
            data.add(new String[] {
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

        final File file = this.createFile(this.splitTestsNotProcessedPath, fileName + this.testExtension);
        file.createNewFile();
        try {
            final FileWriter outputFile = new FileWriter(file);
            final CSVWriter writer = new CSVWriter(outputFile, ';', '"', '"', "\n");
            writer.writeAll(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
