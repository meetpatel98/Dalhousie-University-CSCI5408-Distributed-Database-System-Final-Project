package com.dmwa.Logs.model;

import java.time.Instant;

public class ModelQueryLogs {

    private String queryEntered;
    private static ModelQueryLogs modelQueryLogsInstances = null;

    public static ModelQueryLogs getModelQueryLogsinstances() {
        if (modelQueryLogsInstances == null){
            modelQueryLogsInstances = new ModelQueryLogs();
        }
        return modelQueryLogsInstances;
    }

    public String getQueryEntered() {
        return queryEntered;
    }

    public void setQueryEntered(String queryEntered) {
        this.queryEntered = queryEntered;
    }

    public Instant getQuerySubmissionTs(){
        return Instant.now();
    }

    public long getLogsEntryTs() {
        return Instant.now().toEpochMilli();
    }

}
