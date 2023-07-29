package com.imudataprocessor.api.service;

import java.io.IOException;

public interface ExternalProcess {

    void execute(final InternalDataDTO internalDataDTO) throws IOException;

}
