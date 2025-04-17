package com.example.remitlyintern.Service;

import com.example.remitlyintern.Dto.*;
import com.example.remitlyintern.Model.SwiftCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SwiftCodeService {

    public HeadquarterDTO convertToHeadquaterDTO(SwiftCode swiftCode){
        HeadquarterDTO dto = new HeadquarterDTO();
        dto.setAddress(swiftCode.getAddress());
        dto.setBankName(swiftCode.getBankName());
        dto.setCountryISO2((swiftCode.getCountryIso2()));
        dto.setCountryName(swiftCode.getCountryName());
        dto.setHeadquarter(swiftCode.getHeadquarter());
        dto.setSwiftCode(swiftCode.getSwiftCode());
        return dto;
    }

    public CountryDTO convertSwiftCodeToCountryDTO(SwiftCode swiftCode){
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountryISO2(swiftCode.getCountryISO2());
        return countryDTO;
    }

    public CountryDetailsDTO convertSwiftCodeToCountryDetailsDTO(SwiftCode swiftCode){
        CountryDetailsDTO countryDetailsDTO = new CountryDetailsDTO();
        countryDetailsDTO.setBranchDTOList(convertToBranchDTO(swiftCode.getChildren()));
        countryDetailsDTO.setCountryName(swiftCode.getCountryName());
        countryDetailsDTO.setCountryISO2(swiftCode.getCountryISO2());
        return countryDetailsDTO;
    }

    public BranchWithCountryName convertSwiftCodeToBranchWithCounryName(SwiftCode swiftCode){
        BranchWithCountryName branchWithCountryName = new BranchWithCountryName();
        branchWithCountryName.setCountryName(swiftCode.getCountryName());
        branchWithCountryName.setBankName(swiftCode.getBankName());
        branchWithCountryName.setAddress(swiftCode.getSwiftCode());
        branchWithCountryName.setHeadquarter(swiftCode.getHeadquarter());
        branchWithCountryName.setCountryISO2(swiftCode.getCountryISO2());
        return branchWithCountryName;
    }

    public List<BranchDTO> convertToBranchDTO(List<SwiftCode> children) {
        return children.stream().map(child -> {
            BranchDTO dto = new BranchDTO();
            dto.setAddress(child.getAddress());
            dto.setBankName(child.getBankName());
            dto.setCountryISO2(child.getCountryIso2());
            dto.setHeadquarter(child.getHeadquarter());
            dto.setSwiftCode(child.getSwiftCode());
            return dto;
        }).collect(Collectors.toList());
    }

    public BranchWithCountryName convertSwiftCodeToBranchWithCountryName(SwiftCode child){
        BranchWithCountryName dto = new BranchWithCountryName();
        dto.setAddress(child.getAddress());
        dto.setBankName(child.getBankName());
        dto.setCountryISO2(child.getCountryIso2());
        dto.setHeadquarter(child.getHeadquarter());
        dto.setSwiftCode(child.getSwiftCode());
        dto.setCountryName(child.getCountryName());
        return dto;
    }

    public BranchDTO convertSwiftCodeToBranchDTO(SwiftCode swiftCode){
        BranchDTO branchDTO =  new BranchDTO();
        branchDTO.setCountryISO2(swiftCode.getCountryISO2());
        branchDTO.setHeadquarter(swiftCode.getHeadquarter());
        branchDTO.setAddress(swiftCode.getAddress());
        branchDTO.setBankName(swiftCode.getBankName());
        branchDTO.setSwiftCode(swiftCode.getSwiftCode());
        return branchDTO;
        }

}
