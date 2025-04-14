package com.example.remitlyintern.Service;

import com.example.remitlyintern.Dto.BranchDTO;
import com.example.remitlyintern.Dto.BranchWithCountryName;
import com.example.remitlyintern.Dto.HeadquarterDTO;
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
        dto.setHeadquarter(swiftCode.isHeadquarter());
        dto.setSwiftCode(swiftCode.getSwiftCode());
        
        return dto;
    }

    public List<BranchDTO> convertToBranchDTO(List<SwiftCode> children) {
        return children.stream().map(child -> {
            BranchDTO dto = new BranchDTO();
            dto.setAddress(child.getAddress());
            dto.setBankName(child.getBankName());
            dto.setCountryISO2(child.getCountryIso2());
            dto.setHeadquarter(child.isHeadquarter());
            dto.setSwiftCode(child.getSwiftCode());
            return dto;
        }).collect(Collectors.toList());
    }

    public BranchWithCountryName convertToBranchWithCountryName(SwiftCode child){
        BranchWithCountryName dto = new BranchWithCountryName();
        dto.setAddress(child.getAddress());
        dto.setBankName(child.getBankName());
        dto.setCountryISO2(child.getCountryIso2());
        dto.setHeadquarter(child.isHeadquarter());
        dto.setSwiftCode(child.getSwiftCode());
        dto.setCountryName(child.getCountryName());
        return dto;
    }

}
