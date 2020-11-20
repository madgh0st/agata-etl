package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;

import java.util.Map;

public interface EventLogCounter {

    Map<String, String> process(AbstractCleanDataModel model);
}
