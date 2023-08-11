package com.imudataprocessor.model.service;

import com.google.gson.Gson;
import com.imudataprocessor.api.service.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class JsonServiceImpl implements JsonService {

    @Autowired
    private Gson gson;

    @Value("${grouping_configuration}")
    private String groupingConfigurationFile;

    @Override
    public Object convertToObject(final String json, final Class<?> eClass) throws IOException {
        return this.gson.fromJson(json, eClass);
    }

    @Override
    public Object readFile(final String path, final Class<?> eClass) throws IOException {
        try (final FileReader reader = new FileReader(path)) {
            return this.gson.fromJson(reader, eClass);
        }
    }

    @Override
    public void saveFile(final String path, final Object object) throws IOException {
        try (final FileWriter myWriter = new FileWriter(path)) {
            myWriter.write(this.gson.toJson(object));
        }
    }

}
