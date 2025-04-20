package com.example.remitlyintern.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class H2DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testH2DatabaseConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Failed to connect with H2 DataBase");
            assertEquals("H2", connection.getMetaData().getDatabaseProductName().toUpperCase());
        }
    }
}
