package com.consortium.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Utility class providing SQLite-specific retry logic for write operations.
 * SQLite allows only one writer at a time; under concurrent load writes may fail transiently.
 */
public final class RetryUtil {

    private static final Logger log = LoggerFactory.getLogger(RetryUtil.class);

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 200;

    private RetryUtil() {
    }

    @FunctionalInterface
    public interface WriteOperation<T> {
        T execute();
    }

    public static <T> T executeWithRetry(WriteOperation<T> operation) {
        int attempt = 0;
        while (true) {
            try {
                return operation.execute();
            } catch (DataIntegrityViolationException e) {
                throw e;
            } catch (Exception e) {
                if (isDatabaseLocked(e) && attempt < MAX_RETRIES) {
                    attempt++;
                    log.warn("SQLite database locked, retrying ({}/{})", attempt, MAX_RETRIES);
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted during retry", ie);
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    private static boolean isDatabaseLocked(Throwable e) {
        Throwable cause = e;
        while (cause != null) {
            String msg = cause.getMessage();
            if (msg != null && msg.toLowerCase().contains("database is locked")) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
