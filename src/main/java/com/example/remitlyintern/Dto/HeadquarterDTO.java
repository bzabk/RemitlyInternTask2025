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
public class HeadquarterDTO{

    private String address;
    private String bankName;
    @JsonProperty("isHeadquarter")
    private boolean headquarter;
    private String swiftCode;
    private String countryISO2;


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getCountryISO2() {
        return countryISO2;
    }


    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }


    public String getSwiftCode() {
        return swiftCode;
    }


    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }


    public boolean getHeadquarter() {
        return headquarter;
    }


    public void setHeadquarter(boolean headquarter) {
        this.headquarter = headquarter;
    }


    public String getBankName() {
        return bankName;
    }


    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

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
