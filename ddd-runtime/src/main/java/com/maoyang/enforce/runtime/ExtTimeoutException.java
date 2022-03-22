/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime;

/**
 * 调用扩展点超时抛出的异常.
 */
public class ExtTimeoutException extends RuntimeException {
    private final int timeoutInMs;

    ExtTimeoutException(int timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
    }

    @Override
    public String getMessage() {
        return "timeout:" + timeoutInMs + "ms";
    }
}
