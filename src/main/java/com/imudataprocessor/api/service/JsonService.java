package com.imudataprocessor.api.service;

import java.io.IOException;

public interface JsonService {

    Object convertToObject(String json, Class<?> eClass) throws IOException;

    Object readFile(String path, Class<?> eClass) throws IOException;

    void saveFile(String path, Object object) throws IOException;

}
