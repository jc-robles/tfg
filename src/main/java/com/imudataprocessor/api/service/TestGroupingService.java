package com.imudataprocessor.api.service;

import com.imudataprocessor.api.configuration.GroupingItemConfiguration;
import com.imudataprocessor.api.configuration.TestGropingConfiguration;

import java.io.IOException;

public interface TestGroupingService {

    TestGropingConfiguration getAllGrouping() throws IOException;

    GroupingItemConfiguration addGrouping(String name) throws IOException;

    void removeGrouping(String groupingId) throws IOException;

}
