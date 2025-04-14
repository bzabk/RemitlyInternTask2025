package com.example.remitlyintern.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "address",
        "bankName",
        "countryISO2",
        "countryName",
        "isHeadquarter",
        "swiftCode"
})
public class BranchWithCountryName extends BranchDTO{

    private String CountryName;

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }
}
