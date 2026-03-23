package com.consortium.admin.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Initializes SQLite PRAGMA settings on application startup.
 * These settings optimize SQLite for concurrent access in a server environment.
 */
@Configuration
public class SQLiteConfig {

    private static final Logger log = LoggerFactory.getLogger(SQLiteConfig.class);

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initializePragmas() {
        log.info("Initializing SQLite PRAGMA settings");
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("PRAGMA journal_mode=WAL");
            stmt.execute("PRAGMA synchronous=NORMAL");
            stmt.execute("PRAGMA busy_timeout=5000");

            log.info("SQLite PRAGMA settings applied successfully");
        } catch (SQLException e) {
            log.warn("Could not apply SQLite PRAGMA settings: {}", e.getMessage());
        }
    }
}
