package com.imudataprocessor.api.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TestTypeService {

    List<String> getAllNameTest() throws IOException;

    Optional<ProgramConfiguration> findByTestName(final String testName) throws IOException;

    void createTestType(final String createTest, final MultipartFile fileTest) throws IOException;

    void removeTestType(final String testType) throws IOException;

}
