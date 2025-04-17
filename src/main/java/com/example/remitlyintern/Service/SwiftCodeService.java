package com.example.remitlyintern.Service;

import com.example.remitlyintern.Dto.*;
import com.example.remitlyintern.Model.SwiftCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SwiftCodeService {

    public HeadquarterDTO convertToHeadquaterDTO(SwiftCode swiftCode) {
           return new HeadquarterDTO(
               swiftCode.getAddress(),
               swiftCode.getBankName(),
               swiftCode.getCountryISO2(),
               swiftCode.getCountryName(),
               swiftCode.getHeadquarter(),
               swiftCode.getSwiftCode(),
               convertToBranchDTO(swiftCode.getChildren())
           );
       }

    public CountryDTO convertSwiftCodeToCountryDTO(SwiftCode swiftCode){
        return new CountryDTO(
                swiftCode.getCountryISO2()
        );
    }


    public CountryDetailsDTO convertSwiftCodeToCountryDetailsDTO(SwiftCode swiftCode){
        return new CountryDetailsDTO(
                swiftCode.getCountryName(),
                swiftCode.getCountryISO2()
        );
    }


    public BranchWithCountryName convertSwiftCodeToBranchWithCounryName(SwiftCode swiftCode){
        return new BranchWithCountryName(
            swiftCode.getAddress(),
            swiftCode.getBankName(),
            swiftCode.getHeadquarter(),
            swiftCode.getSwiftCode(),
            swiftCode.getCountryISO2(),
            swiftCode.getCountryName()
        );
    }

    public List<BranchDTO> convertToBranchDTO(List<SwiftCode> children) {
        return children.stream()
                .map(child -> new BranchDTO(
                        child.getAddress(),
                        child.getBankName(),
                        child.getHeadquarter(),
                        child.getSwiftCode(),
                        child.getCountryISO2()))
                .collect(Collectors.toList());
    }

    public BranchWithCountryName convertSwiftCodeToBranchWithCountryName(SwiftCode child) {
        return new BranchWithCountryName(
                child.getAddress(),
                child.getBankName(),
                child.getHeadquarter(),
                child.getSwiftCode(),
                child.getCountryISO2(),
                child.getCountryName()
        );
    }

    public BranchDTO convertSwiftCodeToBranchDTO(SwiftCode swiftCode){
            return new BranchDTO(
                swiftCode.getAddress(),
                swiftCode.getBankName(),
                swiftCode.getHeadquarter(),
                swiftCode.getSwiftCode(),
                swiftCode.getCountryISO2()
            );
        }

}
