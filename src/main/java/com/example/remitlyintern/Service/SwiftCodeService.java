package com.example.remitlyintern.Service;

import com.example.remitlyintern.Dto.*;
import com.example.remitlyintern.Exceptions.*;
import com.example.remitlyintern.Model.SwiftCode;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import com.example.remitlyintern.Utils.CountryISO2Map;
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
    public Object postNewSwiftCodeRecord(PostSwiftCodeDTO postSwiftCodeDTO){

        String countryISOFromSwiftCode = postSwiftCodeDTO.getSwiftCode().substring(4,6);

        if(!CountryISO2Map.countryIsoToCountryMap.containsKey(postSwiftCodeDTO.getCountryISO2())){
            throw new CountryISODoesNotExistException("Provided CountryIso does not exist in a ISO standard");
        }
        if (!postSwiftCodeDTO.getCountryISO2().equalsIgnoreCase(countryISOFromSwiftCode)) {
            throw new CountryISODoesNotMatchWithSwiftCodeException("CountryISO code does not match with 5's and 6's letter from SwiftCode which are responsible for CountryISO");
        }
        if(postSwiftCodeDTO.getSwiftCode().endsWith("XXX") && !postSwiftCodeDTO.Headquarter()){
            throw new HeadquarterAndSwiftCodeConflictException("Provided swiftCode suggests that it is a headquarter but user provided false in headquarter field");
        }
        if(!postSwiftCodeDTO.getSwiftCode().endsWith("XXX") && postSwiftCodeDTO.Headquarter()){
            throw new HeadquarterAndSwiftCodeConflictException("If swiftCode does not end with XXX then it can not be a headquarter");
        }
        if(swiftCodeRepository.existsBySwiftCode(postSwiftCodeDTO.getSwiftCode())){
            throw new SwiftCodeAlreadyExistInDataBaseException("Provided SwiftCode already exists in DataBase");
        }

        SwiftCode newSwiftCode = new SwiftCode(
                postSwiftCodeDTO.getSwiftCode(),
                postSwiftCodeDTO.getBankName(),
                postSwiftCodeDTO.getAddress(),
                postSwiftCodeDTO.getCountryISO2(),
                postSwiftCodeDTO.getCountryName(),
                postSwiftCodeDTO.Headquarter(),
                null,
                null
        );

        if(postSwiftCodeDTO.getSwiftCode().endsWith("XXX")){
            // case when swiftCode is a headquarter

            String mainPart = postSwiftCodeDTO.getSwiftCode().substring(0,9);
            List<SwiftCode> swiftCodesToUpdate = swiftCodeRepository.findAllBySwiftCodeStartingWithAndHeadquarterFalse(mainPart);
            swiftCodesToUpdate.forEach(swiftCode -> {
                swiftCode.setParentSwiftCode(newSwiftCode);
                swiftCodeRepository.save(swiftCode);
            });
        }else{
            // case when swiftcode is not a headquarter
            String mainPart = postSwiftCodeDTO.getSwiftCode().substring(0,8);
            mainPart = mainPart+"XXX";
            if(swiftCodeRepository.existsBySwiftCode(mainPart)){
                SwiftCode foundParent = swiftCodeRepository.findBySwiftCode(mainPart)
                        .orElse(null);
                System.out.println(foundParent);
                newSwiftCode.setParentSwiftCode(foundParent);
            }
        }
        swiftCodeRepository.save(newSwiftCode);
        return "SwiftCode successfully saved in database";
    }

    @Transactional
    public Object deleteRecordBySwiftCode(String swiftCode) {
        SwiftCode swiftCodeToDelete = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new NotFoundElementException("SWIFT code not found in the database"));

        if (swiftCodeToDelete.getHeadquarter() && swiftCodeToDelete.getChildren() != null) {
            for (SwiftCode child : swiftCodeToDelete.getChildren()) {
                child.setParentSwiftCode(null);
                swiftCodeRepository.save(child);
            }
        }
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
