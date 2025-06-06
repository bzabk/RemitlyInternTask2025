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
public class BranchWithCountryName{

    /**
     * DTO is designed to provide the proper display layout for branch data in the '@GetMapping("/{swiftCode}")' request.
     * This DTO is used when provided swiftCode does not end with XXX (swiftCode represent branch bank)
     */

    private String address;
    private String bankName;
    @JsonProperty("isHeadquarter")
    private boolean headquarter;
    private String swiftCode;
    private String countryISO2;
    private String countryName;


    public BranchWithCountryName(String address, String bankName, boolean headquarter, String swiftCode, String countryISO2, String countryName) {
        this.address = address;
        this.bankName = bankName;
        this.headquarter = headquarter;
        this.swiftCode = swiftCode;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isHeadquarter() {
        return headquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        this.headquarter = headquarter;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
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
}
