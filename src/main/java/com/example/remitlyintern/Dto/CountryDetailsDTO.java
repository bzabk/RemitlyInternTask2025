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
    private String countryName;
    @JsonProperty("SwiftCodes")
    private List<BranchDTO> branchDTOList;
    private String countryISO2;


    public String getCountryISO2() {
        return countryISO2;
    }


    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public CountryDetailsDTO() {
        this.branchDTOList = new ArrayList<>();
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<BranchDTO> getBranchDTOList() {
        return branchDTOList;
    }

    public void setBranchDTOList(List<BranchDTO> branchDTOList) {
        this.branchDTOList = branchDTOList;
    }
}
