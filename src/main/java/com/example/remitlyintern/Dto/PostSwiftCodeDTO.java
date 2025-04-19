package com.example.remitlyintern.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PostSwiftCodeDTO {
    @Size(min = 1, max = 100,message = "Length of address must be between 1 and 100")
    private String address;

    @Size(min = 1, max = 100, message = "Length of bankName must be between 1 and 100")
    private String bankName;


    @Pattern(regexp = "^[A-Z]{2}$", message = "Country ISO2 must consist of exactly 2 uppercase letters.")
    @NotBlank(message = "field countryISO2 cannot be empty or contains only white signs")
    private String countryISO2;

    @Size(min = 1, max = 50)
    @NotBlank(message = "field countryName cannot be empty or contains only white signs")
    private String countryName;


    @JsonProperty("isHeadquarter")
    private boolean headquarter;

    @NotBlank(message = "field countryISO2 cannot be empty or contains only white signs")
    @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$", message = "Input does not satisfy required swift code regex")
    private String swiftCode;


    public PostSwiftCodeDTO(String address, String bankName, String countryISO2, String countryName, boolean headquarter, String swiftCode) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.headquarter = headquarter;
        this.swiftCode = swiftCode;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean Headquarter() {
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

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
