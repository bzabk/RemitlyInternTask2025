package com.example.remitlyintern.Controller;

import com.example.remitlyintern.Dto.PostSwiftCodeDTO;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldAssertSwiftCodesTableExists() throws Exception{
        assertNotNull(dataSource, "DataSource cannot be null");
        assertNotNull(jdbcTemplate, "JdbcTemplate cannot be null");
    }


    @Test
    void shouldVerifyTableExistsInH2() {
        Integer countTable = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.tables where table_name = 'SWIFT_CODES_DB'",
                Integer.class);
        assertTrue(countTable > 0, "swift_codes_table does not exist");
    }


    @Test
    void shouldVerifyPostRequestInH2Database(){

        PostSwiftCodeDTO postSwiftCodeDTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "poland",
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
        assertEquals("SwiftCode successfully saved in database, CountryName was standardized to: POLAND",postResponse.getBody());
    }

    @Test
    void shouldVerifyPostAndDeleteRequests() throws Exception{
        PostSwiftCodeDTO postSwiftCodeDTO = new PostSwiftCodeDTO(
                "TEST STREET",
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
                "TEST STREET",
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

    @Test
    void shouldVerifyHeadquarterDeletionUpdatesChildrenReferences() {
        PostSwiftCodeDTO headquarterDTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "Poland",
                true,
                "BANKPLPWXXX"
        );
        HttpEntity<PostSwiftCodeDTO> headquarterRequestEntity = new HttpEntity<>(headquarterDTO);
        testRestTemplate.exchange(
                "/v1/swift-codes",
                HttpMethod.POST,
                headquarterRequestEntity,
                String.class
        );
        PostSwiftCodeDTO branch1DTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "Poland",
                false,
                "BANKPLPW001"
        );
        PostSwiftCodeDTO branch2DTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "Poland",
                false,
                "BANKPLPW002"
        );
        PostSwiftCodeDTO branch3DTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "Poland",
                false,
                "BANKPLPW003"
        );

        HttpEntity<PostSwiftCodeDTO> branch1RequestEntity = new HttpEntity<>(branch1DTO);
        HttpEntity<PostSwiftCodeDTO> branch2RequestEntity = new HttpEntity<>(branch2DTO);
        HttpEntity<PostSwiftCodeDTO> branch3RequestEntity = new HttpEntity<>(branch3DTO);

        testRestTemplate.exchange("/v1/swift-codes", HttpMethod.POST, branch1RequestEntity, String.class);
        testRestTemplate.exchange("/v1/swift-codes", HttpMethod.POST, branch2RequestEntity, String.class);
        testRestTemplate.exchange("/v1/swift-codes", HttpMethod.POST, branch3RequestEntity, String.class);

        var branch1 = swiftCodeRepository.findBySwiftCode("BANKPLPW001").orElseThrow();
        var branch2 = swiftCodeRepository.findBySwiftCode("BANKPLPW002").orElseThrow();
        var branch3 = swiftCodeRepository.findBySwiftCode("BANKPLPW003").orElseThrow();

        assertNotNull(branch1.getParentSwiftCode());
        assertEquals("BANKPLPWXXX", branch1.getParentSwiftCode().getSwiftCode());
        assertNotNull(branch2.getParentSwiftCode());
        assertEquals("BANKPLPWXXX", branch2.getParentSwiftCode().getSwiftCode());
        assertNotNull(branch3.getParentSwiftCode());
        assertEquals("BANKPLPWXXX", branch3.getParentSwiftCode().getSwiftCode());

        testRestTemplate.exchange(
                "/v1/swift-codes/BANKPLPWXXX",
                HttpMethod.DELETE,
                null,
                String.class
        );

        branch1 = swiftCodeRepository.findBySwiftCode("BANKPLPW001").orElseThrow();
        branch2 = swiftCodeRepository.findBySwiftCode("BANKPLPW002").orElseThrow();
        branch3 = swiftCodeRepository.findBySwiftCode("BANKPLPW003").orElseThrow();

        assertNull(branch1.getParentSwiftCode());
        assertNull(branch2.getParentSwiftCode());
        assertNull(branch3.getParentSwiftCode());

        assertFalse(swiftCodeRepository.existsBySwiftCode("BANKPLPWXXX"));
    }


    @Test
    @Transactional
    void shouldVerifyBranchDeletionUpdatesHeadquarterChildrenCollection() {
        PostSwiftCodeDTO headquarterDTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "Poland",
                true,
                "TESTPLPWXXX"
        );
        HttpEntity<PostSwiftCodeDTO> headquarterRequestEntity = new HttpEntity<>(headquarterDTO);
        testRestTemplate.exchange(
                "/v1/swift-codes",
                HttpMethod.POST,
                headquarterRequestEntity,
                String.class
        );
        PostSwiftCodeDTO branch1DTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "Poland",
                false,
                "TESTPLPW001"
        );
        PostSwiftCodeDTO branch2DTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "Poland",
                false,
                "TESTPLPW002"
        );
        PostSwiftCodeDTO branch3DTO = new PostSwiftCodeDTO(
                "TEST STREET",
                "TEST BANK",
                "PL",
                "Poland",
                false,
                "TESTPLPW003"
        );

        HttpEntity<PostSwiftCodeDTO> branch1RequestEntity = new HttpEntity<>(branch1DTO);
        HttpEntity<PostSwiftCodeDTO> branch2RequestEntity = new HttpEntity<>(branch2DTO);
        HttpEntity<PostSwiftCodeDTO> branch3RequestEntity = new HttpEntity<>(branch3DTO);

        testRestTemplate.exchange("/v1/swift-codes", HttpMethod.POST, branch1RequestEntity, String.class);
        testRestTemplate.exchange("/v1/swift-codes", HttpMethod.POST, branch2RequestEntity, String.class);
        testRestTemplate.exchange("/v1/swift-codes", HttpMethod.POST, branch3RequestEntity, String.class);

        var headquarter = swiftCodeRepository.findBySwiftCode("TESTPLPWXXX").orElseThrow();
        assertEquals(3, headquarter.getChildren().size());
        assertTrue(headquarter.getChildren().stream().anyMatch(child -> child.getSwiftCode().equals("TESTPLPW001")));
        assertTrue(headquarter.getChildren().stream().anyMatch(child -> child.getSwiftCode().equals("TESTPLPW002")));
        assertTrue(headquarter.getChildren().stream().anyMatch(child -> child.getSwiftCode().equals("TESTPLPW003")));


        testRestTemplate.exchange(
                "/v1/swift-codes/TESTPLPW002",
                HttpMethod.DELETE,
                null,
                String.class
        );
        // if dabase's table is not refreshed then test fails
        entityManager.clear();
        headquarter = swiftCodeRepository.findBySwiftCode("TESTPLPWXXX").orElseThrow();

        assertEquals(2, headquarter.getChildren().size());
        assertTrue(headquarter.getChildren().stream().anyMatch(child -> child.getSwiftCode().equals("TESTPLPW001")));
        assertTrue(headquarter.getChildren().stream().anyMatch(child -> child.getSwiftCode().equals("TESTPLPW003")));
        assertFalse(headquarter.getChildren().stream().anyMatch(child -> child.getSwiftCode().equals("TESTPLPW002")));


        assertFalse(swiftCodeRepository.existsBySwiftCode("TESTPLPW002"));


        var branch1 = swiftCodeRepository.findBySwiftCode("TESTPLPW001").orElseThrow();
        var branch3 = swiftCodeRepository.findBySwiftCode("TESTPLPW003").orElseThrow();

        assertNotNull(branch1.getParentSwiftCode());
        assertEquals("TESTPLPWXXX", branch1.getParentSwiftCode().getSwiftCode());
        assertNotNull(branch3.getParentSwiftCode());
        assertEquals("TESTPLPWXXX", branch3.getParentSwiftCode().getSwiftCode());
    }



}
