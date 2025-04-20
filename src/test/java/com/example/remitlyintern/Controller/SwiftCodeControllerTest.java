package com.example.remitlyintern.Controller;

import com.example.remitlyintern.Dto.BranchWithCountryName;
import com.example.remitlyintern.Dto.PostSwiftCodeDTO;
import com.example.remitlyintern.Exceptions.CountryISODoesNotExistException;
import com.example.remitlyintern.Exceptions.CountryISODoesNotMatchWithSwiftCodeException;
import com.example.remitlyintern.Exceptions.HeadquarterAndSwiftCodeConflictException;
import com.example.remitlyintern.Exceptions.NotFoundElementException;
import com.example.remitlyintern.Model.SwiftCode;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import com.example.remitlyintern.Service.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SwiftCodeController.class)
public class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SwiftCodeService swiftCodeService;

    @MockitoBean
    private SwiftCodeRepository swiftCodeRepository;


    private SwiftCode createSwiftCode() {
        return new SwiftCode(
                "BCHICLRMIMP",
                "BANCO DE CHILE",
                "",
                "CL",
                "CHILE",
                false,
                null,
                null
        );
    }

    private BranchWithCountryName createBranchDTO() {
        return new BranchWithCountryName(
                " ",
                "BANCO DE CHILE",
                false,
                "BCHICLRMIMP",
                "CL",
                "CHILE"
        );
    }
    //---------------------------------------------------1. Endpoint Tests

    @Test
    void shouldReturnBranchDetailsForValidBranchSwiftCode() throws Exception {

        when(swiftCodeService.getSwiftCodeDetails("BCHICLRMIMP"))
                .thenReturn(createBranchDTO());

        mockMvc.perform(get("/v1/swift-codes/BCHICLRMIMP"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bankName").value("BANCO DE CHILE"))
                .andExpect(jsonPath("$.countryISO2").value("CL"))
                .andExpect(jsonPath("$.countryName").value("CHILE"))
                .andExpect(jsonPath("$.isHeadquarter").value(false))
                .andExpect(jsonPath("$.swiftCode").value("BCHICLRMIMP"));
    }

    @Test
    void shouldReturnBadRequestForSwiftCodeStartingWithNumber() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/2CHICLRMIMP"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Input does not satisfy required swift code regex"));
    }

    @Test
    void shouldReturnBadRequestForSwiftCodeEndingWithNumber() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/BCHICLRMIMP3"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Input does not satisfy required swift code regex"));
    }

    @Test
    void shouldReturnBadRequestForSwiftCodeBeingLowercase() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/bchiclrmimp"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Input does not satisfy required swift code regex"));
    }

    @Test
    void shouldReturnBadRequestForSwiftCodeContainingInvalidCharacters() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/BCHIC)RMIMP"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Input does not satisfy required swift code regex"));
    }

    @Test
    void shouldReturnNotFoundWhenSwiftCodeIsNotInDatabase() throws Exception {
        when(swiftCodeService.getSwiftCodeDetails(any(String.class)))
                .thenThrow(new NotFoundElementException("The provided SWIFT code does not exist in the database"));

        mockMvc.perform(get("/v1/swift-codes/BCHICLQQIMP"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The provided SWIFT code does not exist in the database"));
    }


    //---------------------------------------------------2. Endpoint Tests

    @Test
    void shouldReturnBadRequestWhenCountryIsoCodeHasLessThan2Letters() throws Exception{
        mockMvc.perform(get("/v1/swift-codes/country/B"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Country ISO code must be exactly two uppercase letters"));
    }

    @Test
    void shouldReturnBadRequestWhenCountryIsoCodeContainsNumbers() throws Exception{
        mockMvc.perform(get("/v1/swift-codes/country/34"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Country ISO code must be exactly two uppercase letters"));
    }

    @Test
    void shouldReturnBadRequestWhenCountryIsoHasMoreThan2Signs() throws Exception{
        mockMvc.perform(get("/v1/swift-codes/country/BGL"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Country ISO code must be exactly two uppercase letters"));
    }

    @Test
    void shouldReturnNotFoundWhenCountryIsoNotInDataBase() throws Exception{

        when(swiftCodeService.getSwiftCodesByCountryISO2(any(String.class)))
                .thenThrow(new NotFoundElementException("No Banks with entered countryISO in database"));

        mockMvc.perform(get("/v1/swift-codes/country/QA"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No Banks with entered countryISO in database"));
    }


    @Test
    void shouldReturnSuccessInformationAfterDeletion() throws Exception{
        when(swiftCodeService.deleteRecordBySwiftCode(any(String.class)))
                .thenReturn("Swift Code was deleted successfully");

        mockMvc.perform(delete("/v1/swift-codes/BCHICLRMIMP"))
                .andExpect(status().isOk())
                .andExpect(content().string("Swift Code was deleted successfully"));
    }

    @Test
    void shouldReturnBadRequestWhenSwiftCodeNotInDatBase() throws Exception{
        when(swiftCodeService.deleteRecordBySwiftCode(any(String.class)))
                .thenReturn("The provided SWIFT code does not exist in the database");

        mockMvc.perform(delete("/v1/swift-codes/BCHICLQMIMP"))
                .andExpect(status().isOk())
                .andExpect(content().string("The provided SWIFT code does not exist in the database"));
    }

    @Test
    void shouldReturnBadRequestWhenSwiftCodeDoesNotMatchRegex() throws Exception{
        when(swiftCodeService.deleteRecordBySwiftCode("BCHICL!MIMP"))
                .thenReturn("Input does not satisfy required swift code regex");

        mockMvc.perform(delete("/v1/swift-codes/BCHICL!MIMP"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Input does not satisfy required swift code regex"));
    }

    //-----------------------------------------POST

    @Test
    void shouldReturnBadRequestWhenAdressisLongerThan100Signs() throws Exception{
        StringBuilder longAddress = new StringBuilder();
        longAddress.append("a".repeat(102));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content("""
                {
                    "address": "%s",
                    "bankName": "VELO",
                    "countryISO2": "PL",
                    "countryName": "Polska",
                    "isHeadquarter": true,
                    "swiftCode": "BSCHCLR10RD"
                }
            """.formatted(longAddress)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Length of address must be between 1 and 100"));
    }


    @Test
    void shouldReturnBadRequestWhenBankNameisLongerThan100Signs() throws Exception{
        StringBuilder longBankName = new StringBuilder();
        longBankName.append("a".repeat(102));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content("""
                {
                    "address": "swietokrzyska",
                    "bankName": "%s",
                    "countryISO2": "PL",
                    "countryName": "Polska",
                    "isHeadquarter": true,
                    "swiftCode": "BSCHCLR10RD"
                }
            """.formatted(longBankName)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Length of bankName must be between 1 and 100"));
    }
    // regex w countryIso nie pasuje
    @Test
    void shouldReturnBadRequestWhenCountryISOIsLongerThan2Signs() throws Exception{
        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content("""
                {
                    "address": "swietokrzyska",
                    "bankName": "VELO",
                    "countryISO2": "PLQ",
                    "countryName": "Polska",
                    "isHeadquarter": true,
                    "swiftCode": "BSCHCLR10RD"
                }
            """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Country ISO2 must consist of exactly 2 uppercase letters."));
    }

    @Test
    void shouldReturnBadRequestWhenCountryISOContainsDigits() throws Exception{
        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content("""
                {
                    "address": "swietokrzyska",
                    "bankName": "VELO",
                    "countryISO2": "2P",
                    "countryName": "Polska",
                    "isHeadquarter": true,
                    "swiftCode": "BSCHCLR10RD"
                }
            """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Country ISO2 must consist of exactly 2 uppercase letters."));
    }

    @Test
    void shouldReturnBadRequestWhenCountryISODoesNotMatchWith5and6SignFromSwiftCode() throws Exception{

        when(swiftCodeService.postNewSwiftCodeRecord(any(PostSwiftCodeDTO.class)))
                .thenThrow(new CountryISODoesNotMatchWithSwiftCodeException(
                        "CountryISO code does not match with 5's and 6's letter from SwiftCode which are responsible for CountryISO"));
        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content("""
                {
                "address": "swietokrzyska",
                "bankName": "VELO",
                "countryISO2": "PL",
                "countryName": "Polska",
                "isHeadquarter": true,
                "swiftCode": "BSCHPRR1XXX"
                }
            """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CountryISO code does not match with 5's and 6's letter from SwiftCode which are responsible for CountryISO"));
    }



    @Test
    void shouldReturnBadRequestWhenSwiftCodeDoesNotMatchRegexPost() throws Exception{
        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content("""
                {
                    "address": "swietokrzyska",
                    "bankName": "VELO",
                    "countryISO2": "PL",
                    "countryName": "Polska",
                    "isHeadquarter": true,
                    "swiftCode": "035APLPXXXX"
                }
            """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Input does not satisfy required swift code regex"));
    }

    @Test
    void shouldThrowExceptionWhenSwiftCodeNotEndingWithXXXAndHeadquarterIsTrue() throws Exception {

        when(swiftCodeService.postNewSwiftCodeRecord(any(PostSwiftCodeDTO.class)))
                .thenThrow(new HeadquarterAndSwiftCodeConflictException("Provided swiftCode suggests that it is a headquarter but user provided false in headquarter field"));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content("""
            {
                "address": "swietokrzyska",
                "bankName": "VELO",
                "countryISO2": "PL",
                "countryName": "Polska",
                "isHeadquarter": true,
                "swiftCode": "BSCHCLR10R4"
            }
        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Provided swiftCode suggests that it is a headquarter but user provided false in headquarter field"));
    }

    @Test
    void shouldReturnBadRequestWhenSwiftCodesEndWithXXXAndHeadquarterIsFalse() throws Exception {

        when(swiftCodeService.postNewSwiftCodeRecord(any(PostSwiftCodeDTO.class)))
                .thenThrow(new HeadquarterAndSwiftCodeConflictException("Provided swiftCode suggests that it is a headquarter but user provided false in headquarter field"));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content("""
            {
                "address": "swietokrzyska",
                "bankName": "VELO",
                "countryISO2": "PL",
                "countryName": "Polska",
                "isHeadquarter": false,
                "swiftCode": "BSCHCLR1XXX"
            }
        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Provided swiftCode suggests that it is a headquarter but user provided false in headquarter field"));
    }

    @Test
    void shouldReturnBadRequestWhenCountryISODoesNotExist() throws Exception{
        when(swiftCodeService.postNewSwiftCodeRecord(any(PostSwiftCodeDTO.class)))
                .thenThrow(new CountryISODoesNotExistException("Provided CountryIso does not exist"));

        mockMvc.perform(post("/v1/swift-codes")
                .contentType("application/json")
                .content("""
            {
                "address": "swietokrzyska",
                "bankName": "VELO",
                "countryISO2": "QQ",
                "countryName": "Polska",
                "isHeadquarter": true,
                "swiftCode": "BSCHQQR1XXX"
            }
        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Provided CountryIso does not exist"));

    }







}
