package com.imudataprocessor.api.service;

import java.io.IOException;

public interface ExternalProcess {

    void execute(final String testTypeName, final String nameTest) throws IOException;

}
