package com.imudataprocessor.api.service;

import java.io.IOException;

public interface JsonService {

    Object read(String path, Class<?> eClass) throws IOException;

    void save(String path, Object object) throws IOException;

}
