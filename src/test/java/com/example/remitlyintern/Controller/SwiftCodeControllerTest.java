package com.example.remitlyintern.Controller;

import com.example.remitlyintern.Dto.BranchWithCountryName;
import com.example.remitlyintern.Model.SwiftCode;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import com.example.remitlyintern.Service.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("BCHICLRMIMP");
        swiftCode.setBankName("BANCO DE CHILE");
        swiftCode.setAddress("");
        swiftCode.setHeadquarter(false);
        swiftCode.setCountryIso2("CL");
        swiftCode.setCountryName("CHILE");
        return swiftCode;
    }

    private BranchWithCountryName createBranchDTO() {
        BranchWithCountryName branchDTO = new BranchWithCountryName();
        branchDTO.setSwiftCode("BCHICLRMIMP");
        branchDTO.setBankName("BANCO DE CHILE");
        branchDTO.setAddress("");
        branchDTO.setHeadquarter(false);
        branchDTO.setCountryISO2("CL");
        branchDTO.setCountryName("CHILE");
        return branchDTO;
    }
    //---------------------------------------------------1. Endpoint Tests

    @Test
    void shouldReturnBranchDetailsForValidBranchSwiftCode() throws Exception {
        SwiftCode swiftCode = createSwiftCode();
        BranchWithCountryName branchDTO = createBranchDTO();

        when(swiftCodeRepository.findBySwiftCode("BCHICLRMIMP"))
                .thenReturn(Optional.of(swiftCode));
        when(swiftCodeService.convertSwiftCodeToBranchWithCountryName(swiftCode))
                .thenReturn(branchDTO);

        mockMvc.perform(get("/v1/swift-codes/BCHICLRMIMP"))
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
        mockMvc.perform(get("/v1/swift-codes/country/QA"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No Banks with entered countryISO in database"));
    }






}
