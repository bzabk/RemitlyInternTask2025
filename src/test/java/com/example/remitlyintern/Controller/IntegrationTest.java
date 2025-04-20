package com.example.remitlyintern.Controller;

import com.example.remitlyintern.Dto.PostSwiftCodeDTO;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @Test
    void shouldAssertSwiftCodesTableExists() throws Exception{
        assertNotNull(dataSource, "DataSource cannot be null");
        assertNotNull(jdbcTemplate, "JdbcTemplate cannot be null");
    }


    @Test
    void shouldVerifyTableExistsInH2() {
        Integer countTable = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.tables where table_name = 'SWIFT_CODES_MOD'",
                Integer.class);
        assertTrue(countTable > 0, "swift_codes_table does not exist");
    }


    @Test
    void shouldVerifyPostRequestInH2Database(){

        PostSwiftCodeDTO postSwiftCodeDTO = new PostSwiftCodeDTO(
                "swietokrzyska",
                "TEST BANK",
                "PL",
                "Poland",
                true,
                "ALBPPLPWXXX"
        );
        HttpEntity<PostSwiftCodeDTO> requestEntity = new HttpEntity<>(postSwiftCodeDTO);
        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "/v1/swift-codes",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        assertEquals(200, postResponse.getStatusCode().value());
        assertEquals("SwiftCode successfully saved in database",postResponse.getBody());
    }

    @Test
    void shouldVerifyPostAndDeleteRequests() throws Exception{
        PostSwiftCodeDTO postSwiftCodeDTO = new PostSwiftCodeDTO(
                "swietokrzyska",
                "TEST BANK",
                "PL",
                "Poland",
                true,
                "ARBPPLPWXXX"
        );
        HttpEntity<PostSwiftCodeDTO> requestEntity = new HttpEntity<>(postSwiftCodeDTO);
        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "/v1/swift-codes",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        assertEquals(200, postResponse.getStatusCode().value());
        assertEquals("SwiftCode successfully saved in database",postResponse.getBody());

        ResponseEntity<String> deleteResponse = testRestTemplate.exchange(
                "/v1/swift-codes/ARBPPLPWXXX",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertEquals(200, deleteResponse.getStatusCode().value());
        assertEquals("Swift Code was deleted successfully",deleteResponse.getBody());

        assertFalse(swiftCodeRepository.existsBySwiftCode("ARBPPLPWXXX"));
    }

    @Test
    void shouldReturnBadRequestAfterTwoDeleteRequest() throws Exception{
        PostSwiftCodeDTO postSwiftCodeDTO = new PostSwiftCodeDTO(
                "swietokrzyska",
                "TEST BANK",
                "PL",
                "Poland",
                true,
                "ARBPPLPWXXX"
        );
        HttpEntity<PostSwiftCodeDTO> requestEntity = new HttpEntity<>(postSwiftCodeDTO);
        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "/v1/swift-codes",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        assertEquals(200, postResponse.getStatusCode().value());
        assertEquals("SwiftCode successfully saved in database",postResponse.getBody());

        ResponseEntity<String> deleteResponse = testRestTemplate.exchange(
                "/v1/swift-codes/ARBPPLPWXXX",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertEquals(200, deleteResponse.getStatusCode().value());
        assertEquals("Swift Code was deleted successfully",deleteResponse.getBody());

        assertFalse(swiftCodeRepository.existsBySwiftCode("ARBPPLPWXXX"));


        ResponseEntity<String> deleteResponseRepeated = testRestTemplate.exchange(
                "/v1/swift-codes/ARBPPLPWXXX",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertEquals(404, deleteResponseRepeated .getStatusCode().value());
        assertEquals("SWIFT code not found in the database",deleteResponseRepeated .getBody());

    }





}
