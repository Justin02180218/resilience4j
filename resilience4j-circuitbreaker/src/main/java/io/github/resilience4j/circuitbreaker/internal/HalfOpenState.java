/*
 *
 *  Copyright 2016 Robert Winkler
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package io.github.resilience4j.circuitbreaker.internal;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

final class HalfOpenState extends CircuitBreakerState {
    // 半开状态的度量指标
    private CircuitBreakerMetrics circuitBreakerMetrics;
    // 请求调用的失败率阈值，在配置类CircuitBreakerConfig的实例中已设置
    private final float failureRateThreshold;

    HalfOpenState(CircuitBreakerStateMachine stateMachine) {
        super(stateMachine);
        CircuitBreakerConfig circuitBreakerConfig = stateMachine.getCircuitBreakerConfig();
        this.circuitBreakerMetrics = new CircuitBreakerMetrics(
                circuitBreakerConfig.getRingBufferSizeInHalfOpenState());
        this.failureRateThreshold = stateMachine.getCircuitBreakerConfig().getFailureRateThreshold();
    }

    /**
     * Returns always true, because the CircuitBreaker is half open.
     *
     * @return always true, because the CircuitBreaker is half open.
     */
    @Override
    boolean isCallPermitted() {
        return true;
    }

    @Override
    void onError(Throwable throwable) {
        // CircuitBreakerMetrics is thread-safe
        checkFailureRate(circuitBreakerMetrics.onError());
    }

    @Override
    void onSuccess() {
        // CircuitBreakerMetrics is thread-safe
        checkFailureRate(circuitBreakerMetrics.onSuccess());
    }

    /**
     * 如果当前失败率大于等于阈值，则当前状态转换到打开状态。
     * 否则，当前状态转换到关闭状态
     * Checks if the current failure rate is above or below the threshold.
     * If the failure rate is above the threshold, transition the state machine to OPEN state.
     * If the failure rate is below the threshold, transition the state machine to CLOSED state.
     * @param currentFailureRate the current failure rate
     */
    private void checkFailureRate(float currentFailureRate) {
        if(currentFailureRate != -1){
            if(currentFailureRate >= failureRateThreshold) {
                stateMachine.transitionToOpenState();
            }else{
                stateMachine.transitionToClosedState();
            }
        }
    }

    /**
     * Get the state of the CircuitBreaker
     */
    @Override
    CircuitBreaker.State getState() {
        return CircuitBreaker.State.HALF_OPEN;
    }

    @Override
    CircuitBreakerMetrics getMetrics() {
        return circuitBreakerMetrics;
    }
}
