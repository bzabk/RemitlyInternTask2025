package com.example.remitlyintern.Service;

import com.example.remitlyintern.Dto.*;
import com.example.remitlyintern.Exceptions.NotFoundElementException;
import com.example.remitlyintern.Model.SwiftCode;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SwiftCodeService {

    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftCodeService(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }


    @Transactional
    public Object deleteRecordBySwiftCode(String swiftCode){
        SwiftCode foundSwiftCode = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new NotFoundElementException("The provided SWIFT code does not exist in the database"));
        swiftCodeRepository.deleteSwiftCodeBySwiftCode(swiftCode);
        return "Swift Code was deleted successfully";
    }

    public Object getSwiftCodeDetails(String swiftCode) {
        SwiftCode foundSwiftCode = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new NotFoundElementException("The provided SWIFT code does not exist in the database"));

        if (foundSwiftCode.getHeadquarter()) {
            HeadquarterDTO headquarterDTO = convertToHeadquaterDTO(foundSwiftCode);
            headquarterDTO.setBranches(convertToBranchDTO(foundSwiftCode.getChildren()));
            return headquarterDTO;
        } else {
            return convertSwiftCodeToBranchWithCountryName(foundSwiftCode);
        }
    }

    public Object getSwiftCodesByCountryISO2(String countryISO2){
        List<SwiftCode> listOfSwiftCodes = swiftCodeRepository.findAllByCountryISO2(countryISO2);

        if(listOfSwiftCodes.isEmpty()){
            throw new NotFoundElementException("No Banks with entered countryISO in database");
        }

        CountryDetailsDTO countryDetailsDTO = new CountryDetailsDTO(countryISO2,
                listOfSwiftCodes.stream()
                        .findFirst()
                        .map(SwiftCode::getCountryName)
                        .orElse(""));

        listOfSwiftCodes.forEach(swiftCode -> {
            countryDetailsDTO.getBranchDTOList().add(convertSwiftCodeToBranchDTO(swiftCode));
        });
        return countryDetailsDTO;
    }

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
