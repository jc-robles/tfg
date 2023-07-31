package com.imudataprocessor.api.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;

import java.io.IOException;
import java.util.List;

public interface ExternalProcess {

    void createNewProgramToExecute(final ProgramConfiguration programConfiguration, byte[] programFile) throws IOException;

    List<String> getAllNameTest() throws IOException;

    void execute(final String testTypeName, final String nameTest) throws IOException;

}
