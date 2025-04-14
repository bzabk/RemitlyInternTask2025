package com.example.remitlyintern.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
@JsonPropertyOrder({
        "countryISO2",
        "countryName"
})
public class CountryDetailsDTO extends CountryDTO{
    private String countryName;
    private List<BranchDTO> branchDTOList;
}
