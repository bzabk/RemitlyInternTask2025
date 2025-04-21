package com.example.remitlyintern.Service;

import com.example.remitlyintern.Dto.*;
import com.example.remitlyintern.Exceptions.*;
import com.example.remitlyintern.Model.SwiftCode;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import com.example.remitlyintern.Utils.SwiftCodeMapper;
import com.example.remitlyintern.Utils.SwiftCodeValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SwiftCodeService {

    private final SwiftCodeRepository swiftCodeRepository;
    private final SwiftCodeValidator swiftCodeValidator;

    public SwiftCodeService(SwiftCodeRepository swiftCodeRepository,
                            SwiftCodeValidator swiftCodeValidator) {
        this.swiftCodeRepository = swiftCodeRepository;
        this.swiftCodeValidator = swiftCodeValidator;
    }


    @Transactional
    public Object postNewSwiftCodeRecord(PostSwiftCodeDTO postSwiftCodeDTO){

        swiftCodeValidator.validateSwiftCodeForCreation(postSwiftCodeDTO);

        SwiftCode newSwiftCode = SwiftCodeMapper.toEntity(postSwiftCodeDTO);

        setupSwiftCodeRelationships(newSwiftCode);
        swiftCodeRepository.save(newSwiftCode);
        return "SwiftCode successfully saved in database";
    }

    private void setupSwiftCodeRelationships(SwiftCode swiftCode) {
        if (swiftCode.getHeadquarter()) {
            setupBranchesToHeadquarter(swiftCode);
        } else {
            assignToExistingHeadquarter(swiftCode);
        }
    }

    private void assignToExistingHeadquarter(SwiftCode branch) {
        String headquarterSwiftCode = branch.getSwiftCode().substring(0, 8) + "XXX";
        swiftCodeRepository.findBySwiftCode(headquarterSwiftCode)
                .ifPresent(branch::setParentSwiftCode);
    }

    private void setupBranchesToHeadquarter(SwiftCode headquarter) {
        String mainPart = headquarter.getSwiftCode().substring(0, 9);
        List<SwiftCode> branches = swiftCodeRepository.findAllBySwiftCodeStartingWithAndHeadquarterFalse(mainPart);
        branches.forEach(branch -> {
            branch.setParentSwiftCode(headquarter);
            swiftCodeRepository.save(branch);
        });
    }

    @Transactional
    public Object deleteRecordBySwiftCode(String swiftCode) {
        SwiftCode swiftCodeToDelete = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new RecordNotFoundException("SWIFT code not found in the database"));

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
                .orElseThrow(() -> new RecordNotFoundException("The provided SWIFT code does not exist in the database"));

        if (foundSwiftCode.getHeadquarter()) {
            return SwiftCodeMapper.toHeadquarterDTO(foundSwiftCode);
        } else {
            return SwiftCodeMapper.toBranchWithCountryName(foundSwiftCode);
        }
    }

    public CountryDetailsDTO getSwiftCodesByCountryISO2(String countryISO2) {
        List<SwiftCode> swiftCodes = swiftCodeRepository.findAllByCountryISO2(countryISO2);

        if (swiftCodes.isEmpty()) {
            throw new RecordNotFoundException("No Banks with entered countryISO in database");
        }

        String countryName = swiftCodes.getFirst().getCountryName();
        CountryDetailsDTO countryDetails = new CountryDetailsDTO(countryISO2, countryName);

        List<BankBranchDTO> branches = swiftCodes.stream()
                .map(SwiftCodeMapper::toBranchDTO)
                .toList();

        countryDetails.getBranchDTOList().addAll(branches);
        return countryDetails;
    }


}
