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

    /**
     * DTO is designed to provide the proper display layout for branch data in the '@GetMapping("/{swiftCode}")' request.
     * This DTO is used when provided swiftCode ends with XXX (swiftCode represents headquarter bank)
     */

    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    @JsonProperty("isHeadquarter")
    private boolean headquarter;
    private String swiftCode;
    private List<BankBranchDTO> branches;

    public HeadquarterDTO(String address, String bankName, String countryISO2, String countryName, boolean headquarter, String swiftCode, List<BankBranchDTO> branches) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.headquarter = headquarter;
        this.swiftCode = swiftCode;
        this.branches = branches;
    }

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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<BankBranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BankBranchDTO> branches) {
        this.branches = branches;
    }
}
