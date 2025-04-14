package com.example.remitlyintern.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "address",
        "bankName",
        "countryISO2",
        "countryName",
        "isHeadquarter",
        "swiftCode",
        "branches"
})
public class HeadquarterDTO extends BranchDTO {


    private List<BranchDTO> branches;

    private String countryName;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDTO> branches) {
        this.branches = branches;
    }
}
