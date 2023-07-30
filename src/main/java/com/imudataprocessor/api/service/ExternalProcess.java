package com.imudataprocessor.api.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;

import java.io.File;
import java.io.IOException;

public interface ExternalProcess {

    void createNewProgramToExecute(final ProgramConfiguration programConfiguration, byte[] programFile) throws IOException;

    void execute(final InternalDataDTO internalDataDTO) throws IOException;

}
