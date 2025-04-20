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
@ActiveProfiles("prod")
public class PostGresDataBaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testPostGresDataBaseConnection() throws Exception{
        try(Connection connection = dataSource.getConnection()){
            assertNotNull(connection, "Failed to connect with PostGres DataBase");
            assertEquals("POSTGRESQL", connection.getMetaData().getDatabaseProductName().toUpperCase());
        }
    }
}
