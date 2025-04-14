package com.example.remitlyintern.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "address",
        "bankName",
        "countryISO2",
        "isHeadquarter",
        "swiftCode"
})
public class BranchDTO extends CountryDTO {
    private String address;
    private String bankName;
    @JsonProperty("isHeadquarter")
    private boolean Headquarter;
    private String swiftCode;


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

    public boolean Headquarter() {
        return Headquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        Headquarter = headquarter;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


}
