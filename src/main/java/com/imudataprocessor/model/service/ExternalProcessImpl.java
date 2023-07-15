package com.imudataprocessor.model.service;

import com.imudataprocessor.api.service.ExternalProcess;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class ExternalProcessImpl implements ExternalProcess {

    @Override
    public void execute(String nameTest) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/python/Tratamiento de datos.py");
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
    }

}
