package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.dto.internal.InternalDataDTO;
import com.imudataprocessor.api.dto.out.processedtest.OutputDataDTO;
import com.imudataprocessor.api.service.FileService;
import com.imudataprocessor.api.service.JsonService;
import com.imudataprocessor.model.mapper.CSVWriterDataMapper;
import com.imudataprocessor.model.mapper.InternalDataDTOMapper;
import com.imudataprocessor.model.mapper.OutputDataDTOMapper;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private JsonService jsonService;

    @Autowired
    private OutputDataDTOMapper outputDataDTOMapper;

    @Autowired
    private InternalDataDTOMapper internalDataDTOMapper;

    @Autowired
    private CSVWriterDataMapper csvWriterDataMapper;

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

    @Value("${separator}")
    private char separator;

    @Override
    public void save(final String path, final String fileName, final byte[] bytes) throws IOException {
        final File file = this.createFile(path, fileName);
        try (final OutputStream os = new FileOutputStream(file)) {
            os.write(bytes);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File split(final String fileName, final Integer start, final Integer end) throws IOException {
        final InternalDataDTO dataObtained = this.obtainCompleteDataToFile(this.mainFilePath);
        final InternalDataDTO splitData = this.splitData(dataObtained, start, end);
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
    public void deleteAllTest() throws IOException {
        Files.walk(Paths.get(this.originFilePath)).forEach(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (final IOException e) {
            }
        });
    }

    private InternalDataDTO obtainDataToFile(final @NonNull String filePath) throws IOException {
        final InternalDataDTO dataDTO = this.obtainCompleteDataToFile(filePath);
        this.deleteHeaders(dataDTO);
        return dataDTO;
    }

    @Override
    public OutputDataDTO obtainDataToFileProcessed(final Optional<ProgramConfiguration> programConfiguration, final @NonNull String filePath) throws IOException {
        final Map<String, Object> data = (Map<String, Object>) this.jsonService.readFile(this.testsProcessedPath + "/" + filePath + ".json", Map.class);
        return this.outputDataDTOMapper.map(programConfiguration, data);
    }

    private InternalDataDTO obtainCompleteDataToFile(final @NonNull String filePath) throws IOException {
        final Optional<File> file = Files.walk(Paths.get(filePath)).filter(Files::isRegularFile).map(Path::toFile).findFirst();
        return file.map(this.internalDataDTOMapper::map).orElse(new InternalDataDTO());
    }

    private InternalDataDTO splitData(final InternalDataDTO internalDataDTO, final Integer start, final Integer end) {
        final int fileStart = Objects.isNull(start) ? 0 : start;
        final int fileEnd = Objects.isNull(end) ? internalDataDTO.getAccelerometerX().size() : end + 2;
        final InternalDataDTO internalDataSplit = new InternalDataDTO();
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
        final List<String[]> data = this.csvWriterDataMapper.map(dataObtained);
        final File file = this.createFile(this.splitTestsNotProcessedPath, fileName + this.testExtension);
        file.createNewFile();
        try {
            final FileWriter outputFile = new FileWriter(file);
            final CSVWriter writer = new CSVWriter(outputFile, this.separator, '"', '"', "\n");
            writer.writeAll(data);
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
