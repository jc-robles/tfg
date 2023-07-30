package com.imudataprocessor.model.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imudataprocessor.api.service.JsonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class JsonServiceImpl implements JsonService {

    @Value("${grouping_configuration}")
    private String groupingConfigurationFile;

    @Override
    public Object convertToObject(String json, Class<?> eClass) throws IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, eClass);
    }

    @Override
    public Object readFile(final String path, final Class<?> eClass) throws IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(new FileReader(path), eClass);
    }

    @Override
    public void saveFile(final String path, final Object object) throws IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter myWriter = new FileWriter(path);
        myWriter.write(gson.toJson(object));
        myWriter.close();
    }

}
