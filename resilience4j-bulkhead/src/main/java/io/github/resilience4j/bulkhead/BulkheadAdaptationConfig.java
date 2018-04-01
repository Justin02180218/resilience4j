package io.github.resilience4j.bulkhead;

import java.time.Duration;

public class BulkheadAdaptationConfig {

    // TODO:bstorozhuk finish this

    private double desirableAverageThroughput; // in req/sec
    private double desirableOperationLatency; // in sec/op
    private double maxAcceptableRequestLatency; // in sec/op
    private Duration windowForAdaptation;
    private Duration windowForReconfiguration;

    public BulkheadAdaptationConfig(double desirableAverageThroughput, double desirableOperationLatency, double maxAcceptableRequestLatency, Duration windowForAdaptation, Duration windowForReconfiguration) {
        this.desirableAverageThroughput = desirableAverageThroughput;
        this.desirableOperationLatency = desirableOperationLatency;
        this.maxAcceptableRequestLatency = maxAcceptableRequestLatency;
        this.windowForAdaptation = windowForAdaptation;
        this.windowForReconfiguration = windowForReconfiguration;
    }

    public double getDesirableAverageThroughput() {
        return desirableAverageThroughput;
    }

    public double getDesirableOperationLatency() {
        return desirableOperationLatency;
    }

    public double getMaxAcceptableRequestLatency() {
        return maxAcceptableRequestLatency;
    }

    public Duration getWindowForAdaptation() {
        return windowForAdaptation;
    }

    public Duration getWindowForReconfiguration() {
        return windowForReconfiguration;
    }
}