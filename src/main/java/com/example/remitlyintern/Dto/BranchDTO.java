package com.example.remitlyintern.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "address",
        "bankName",
        "countryISO2",
        "isHeadquarter",
        "swiftCode"
})
public class BranchDTO {
    private String address;
    private String bankName;
    @JsonProperty("isHeadquarter")
    private boolean headquarter;
    private String swiftCode;
    private String countryISO2;

    public BranchDTO(String address, String bankName, boolean headquarter, String swiftCode, String countryISO2) {
        this.address = address;
        this.bankName = bankName;
        this.headquarter = headquarter;
        this.swiftCode = swiftCode;
        this.countryISO2 = countryISO2;
    }

    public boolean getHeadquarter() {
        return headquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        this.headquarter = headquarter;
    }

    public String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


}
