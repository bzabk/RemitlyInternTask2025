package com.example.remitlyintern.Utils;

import com.example.remitlyintern.Dto.BankBranchDTO;
import com.example.remitlyintern.Dto.BranchWithCountryName;
import com.example.remitlyintern.Dto.HeadquarterDTO;
import com.example.remitlyintern.Dto.PostSwiftCodeDTO;
import com.example.remitlyintern.Model.SwiftCode;

import java.util.List;
import java.util.stream.Collectors;

public class SwiftCodeMapper {

    public static SwiftCode toEntity(PostSwiftCodeDTO dto) {
        return new SwiftCode(
                dto.getSwiftCode(),
                dto.getBankName(),
                dto.getAddress(),
                dto.getCountryISO2(),
                dto.getCountryName(),
                dto.Headquarter(),
                null,
                null
        );
    }


    public static HeadquarterDTO toHeadquarterDTO(SwiftCode swiftCode) {
        return new HeadquarterDTO(
                swiftCode.getAddress(),
                swiftCode.getBankName(),
                swiftCode.getCountryISO2(),
                swiftCode.getCountryName(),
                swiftCode.getHeadquarter(),
                swiftCode.getSwiftCode(),
                toBranchDTOList(swiftCode.getChildren())
        );
    }

    public static List<BankBranchDTO> toBranchDTOList(List<SwiftCode> children) {
        return children.stream()
                .map(SwiftCodeMapper::toBranchDTO)
                .collect(Collectors.toList());
    }

    public static BankBranchDTO toBranchDTO(SwiftCode swiftCode) {
        return new BankBranchDTO(
                swiftCode.getAddress(),
                swiftCode.getBankName(),
                swiftCode.getHeadquarter(),
                swiftCode.getSwiftCode(),
                swiftCode.getCountryISO2()
        );
    }

    public static BranchWithCountryName toBranchWithCountryName(SwiftCode swiftCode) {
        return new BranchWithCountryName(
                swiftCode.getAddress(),
                swiftCode.getBankName(),
                swiftCode.getHeadquarter(),
                swiftCode.getSwiftCode(),
                swiftCode.getCountryISO2(),
                swiftCode.getCountryName()
        );
    }
}
