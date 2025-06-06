package com.example.remitlyintern.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;
@JsonPropertyOrder({
        "countryISO2",
        "countryName",
        "SwiftCodes"
})
public class CountryDetailsDTO{

    /**
     * DTO is designed to provide the proper display layout in the '@GetMapping("/country/{countryISO2code}")' request.
     */

    private String countryName;
    @JsonProperty("SwiftCodes")
    private List<BankBranchDTO> branchDTOList;
    private String countryISO2;

    public CountryDetailsDTO(String countryName, String countryISO2) {
        this.countryName = countryName;
        this.branchDTOList = new ArrayList<>();
        this.countryISO2 = countryISO2;
    }

    public String getCountryISO2() {
        return countryISO2;
    }


    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }


    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<BankBranchDTO> getBranchDTOList() {
        return branchDTOList;
    }

    public void setBranchDTOList(List<BankBranchDTO> branchDTOList) {
        this.branchDTOList = branchDTOList;
    }
}
