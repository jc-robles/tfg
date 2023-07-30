package com.imudataprocessor.controller;

import com.imudataprocessor.api.controller.DataDTO;
import com.imudataprocessor.api.controller.FileController;
import com.imudataprocessor.api.service.ExternalProcess;
import com.imudataprocessor.api.service.InternalDataDTO;
import com.imudataprocessor.api.service.ProcessDataService;
import com.imudataprocessor.model.mapper.DataDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Autowired
    private ExternalProcess externalProcess;

    @PostMapping("/upload-file")
    public ResponseEntity<DataDTO> uploadFile(final @RequestParam("file") MultipartFile multipartFile,
            final Model model) throws IOException {
        final InternalDataDTO internalDataDTO = this.processDataServiceImpl.processMainTest(multipartFile);

        externalProcess.execute(internalDataDTO);

        return ResponseEntity.ok(dataDTOMapper.map(internalDataDTO));
    }

    @PostMapping("/split-file")
    public ResponseEntity<DataDTO> splitFile(final @RequestParam("fileName") String fileName,
            final @RequestParam("start") Integer start, final @RequestParam("end") Integer end) throws IOException {
        final InternalDataDTO internalDataDTO = this.processDataServiceImpl.processSplitTest(fileName, start, end);
        return ResponseEntity.ok(dataDTOMapper.map(internalDataDTO));
    }

    @PostMapping("/process-test")
    public ResponseEntity<DataDTO> processFile(final @RequestParam("fileName") String fileName) throws IOException {
        final InternalDataDTO internalDataDTO = this.processDataServiceImpl.processDataTest(fileName);
        return ResponseEntity.ok(dataDTOMapper.map(internalDataDTO));
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<HttpStatus> deleteFile(final @RequestParam("fileName") String fileName) throws IOException {
        this.processDataServiceImpl.deleteTest(fileName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-all-file")
    public ResponseEntity<HttpStatus> deleteAllFile() throws IOException {
        this.processDataServiceImpl.deleteAllTest();
        return ResponseEntity.ok().build();
    }
}
