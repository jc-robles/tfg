package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.GroupingItemConfiguration;
import com.imudataprocessor.api.configuration.TestGropingConfiguration;
import com.imudataprocessor.api.service.JsonService;
import com.imudataprocessor.api.service.TestGroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class TestGroupingServiceImpl implements TestGroupingService {

    @Value("${grouping_Configuration}")
    private String groupingConfigurationFile;

    @Autowired
    private JsonService jsonService;

    @Override
    public TestGropingConfiguration getAllGrouping() throws IOException {
        return (TestGropingConfiguration) jsonService.read(groupingConfigurationFile, TestGropingConfiguration.class);
    }

    @Override
    public GroupingItemConfiguration addGrouping(final String name) throws IOException {
        TestGropingConfiguration testGropingConfiguration =
                (TestGropingConfiguration) jsonService.read(groupingConfigurationFile, TestGropingConfiguration.class);
        if (Objects.isNull(testGropingConfiguration.getSelect())) {
            testGropingConfiguration.setSelect(new ArrayList<>());
        }
        final GroupingItemConfiguration groupingItemConfiguration =
                GroupingItemConfiguration.of(name.toLowerCase().replace(" ", "_") + "_id", name, name);
        testGropingConfiguration.getSelect().add(groupingItemConfiguration);
        jsonService.save(groupingConfigurationFile, testGropingConfiguration);
        return groupingItemConfiguration;
    }

    @Override
    public void removeGrouping(String groupingId) throws IOException {
        TestGropingConfiguration testGropingConfiguration =
                (TestGropingConfiguration) jsonService.read(groupingConfigurationFile, TestGropingConfiguration.class);
        if (Objects.nonNull(testGropingConfiguration.getSelect())) {
            testGropingConfiguration.getSelect().stream()
                    .filter(groupingItemConfiguration1 -> ObjectUtils.nullSafeEquals(groupingItemConfiguration1.getId(), groupingId))
                    .findAny()
                    .ifPresent(groupingItemConfiguration1 -> testGropingConfiguration.getSelect().remove(groupingItemConfiguration1));
        }
        jsonService.save(groupingConfigurationFile, testGropingConfiguration);
    }

}
