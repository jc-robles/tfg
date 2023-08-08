package com.imudataprocessor.controller.file;

import com.imudataprocessor.api.controller.file.FileController;
import com.imudataprocessor.api.dto.internal.InternalDataDTO;
import com.imudataprocessor.api.dto.out.processedtest.OutputDataDTO;
import com.imudataprocessor.api.dto.out.test.DataDTO;
import com.imudataprocessor.api.service.ProcessDataService;
import com.imudataprocessor.model.mapper.DataDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileControllerImpl implements FileController {

    @Autowired
    private ProcessDataService processDataServiceImpl;

    @Autowired
    private DataDTOMapper dataDTOMapper;

    @Value("${tests-processed-path}")
    private String testsProcessedPath;

    @Value("${split-tests-not-processed-path}")
    private String splitTestsNotProcessedPath;

    @Override
    @PostMapping("/upload-file")
    public ResponseEntity<DataDTO> uploadFile(final @RequestParam("file") MultipartFile multipartFile,
                                              final Model model) throws IOException {
        final InternalDataDTO internalDataDTO = this.processDataServiceImpl.processMainTest(multipartFile);
        return ResponseEntity.ok(this.dataDTOMapper.map(internalDataDTO));
    }

    @Override
    @PostMapping("/split-file")
    public ResponseEntity<DataDTO> splitFile(final @RequestParam("fileName") String fileName,
                                             final @RequestParam("start") Integer start, final @RequestParam("end") Integer end) throws IOException {
        final InternalDataDTO internalDataDTO = this.processDataServiceImpl.processSplitTest(fileName, start, end);
        return ResponseEntity.ok(this.dataDTOMapper.map(internalDataDTO));
    }

    @Override
    @PostMapping("/process-test")
    public ResponseEntity<OutputDataDTO> processFile(final @RequestParam("testTypeName") String testTypeName,
                                                     final @RequestParam("fileName") String fileName) throws IOException {
        final OutputDataDTO outputDataDTO = this.processDataServiceImpl.processDataTest(testTypeName, fileName);
        return ResponseEntity.ok(outputDataDTO);
    }

    @Override
    @DeleteMapping("/delete-file")
    public ResponseEntity<HttpStatus> deleteFile(final @RequestParam("fileName") String fileName) throws IOException {
        this.processDataServiceImpl.deleteTest(fileName);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/delete-all-file")
    public ResponseEntity<HttpStatus> deleteAllFile() throws IOException {
        this.processDataServiceImpl.deleteAllTest();
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/download-processed-test")
    public ResponseEntity<FileSystemResource> downloadProcessedTest(final @RequestParam("nameTest") String nameTest) {
        final String pathFile = this.testsProcessedPath + "/" + nameTest + ".json";
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nameTest + ".json");
        return ResponseEntity.ok().headers(headers).body(new FileSystemResource(pathFile));
    }

    @Override
    @GetMapping("/download-raw-test")
    public ResponseEntity<FileSystemResource> downloadRawTest(final @RequestParam("nameTest") String nameTest) {
        final String pathFile = this.splitTestsNotProcessedPath + "/" + nameTest + ".csv";
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nameTest + ".csv");
        return ResponseEntity.ok().headers(headers).body(new FileSystemResource(pathFile));
    }
}
