package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.GraphItemConfiguration;
import com.imudataprocessor.api.configuration.TestGraphConfiguration;
import com.imudataprocessor.api.service.JsonService;
import com.imudataprocessor.api.service.TestGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class TestGraphServiceImpl implements TestGraphService {

    @Value("${graph_configuration}")
    private String graphConfigurationFile;

    @Autowired
    private JsonService jsonService;

    @Override
    public TestGraphConfiguration getAllGraphs() throws IOException {
        return (TestGraphConfiguration) this.jsonService.readFile(this.graphConfigurationFile, TestGraphConfiguration.class);
    }

    @Override
    public GraphItemConfiguration addGraph(final String name) throws IOException {
        final TestGraphConfiguration testGraphConfiguration =
                (TestGraphConfiguration) this.jsonService.readFile(this.graphConfigurationFile, TestGraphConfiguration.class);
        if (Objects.isNull(testGraphConfiguration.getSelect())) {
            testGraphConfiguration.setSelect(new ArrayList<>());
        }
        final GraphItemConfiguration graphItemConfiguration =
                GraphItemConfiguration.of(name.toLowerCase().replace(" ", "_") + "_id", name, name);
        testGraphConfiguration.getSelect().add(graphItemConfiguration);
        this.jsonService.saveFile(this.graphConfigurationFile, testGraphConfiguration);
        return graphItemConfiguration;
    }

    @Override
    public void removeGraph(final String graphId) throws IOException {
        final TestGraphConfiguration testGraphConfiguration =
                (TestGraphConfiguration) this.jsonService.readFile(this.graphConfigurationFile, TestGraphConfiguration.class);
        if (Objects.nonNull(testGraphConfiguration.getSelect())) {
            testGraphConfiguration.getSelect().stream()
                    .filter(graphItemConfiguration1 -> ObjectUtils.nullSafeEquals(graphItemConfiguration1.getId(), graphId))
                    .findAny()
                    .ifPresent(graphItemConfiguration1 -> testGraphConfiguration.getSelect().remove(graphItemConfiguration1));
        }
        this.jsonService.saveFile(this.graphConfigurationFile, testGraphConfiguration);
    }

}
