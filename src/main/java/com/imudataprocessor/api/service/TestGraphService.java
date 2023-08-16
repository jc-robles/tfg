package com.imudataprocessor.api.service;

import com.imudataprocessor.api.configuration.GraphItemConfiguration;
import com.imudataprocessor.api.configuration.TestGraphConfiguration;

import java.io.IOException;

public interface TestGraphService {

    TestGraphConfiguration getAllGraphs() throws IOException;

    GraphItemConfiguration addGraph(String name) throws IOException;

    void removeGraph(String graphId) throws IOException;

}
