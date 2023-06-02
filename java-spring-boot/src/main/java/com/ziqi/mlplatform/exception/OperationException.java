package com.ziqi.mlplatform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperationException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(OperationException.class);
    public OperationException(String message) {
        super(message);
        log.error(message);
    }
}
