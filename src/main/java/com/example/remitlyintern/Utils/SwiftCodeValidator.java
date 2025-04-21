package com.example.remitlyintern.Utils;

import com.example.remitlyintern.Dto.PostSwiftCodeDTO;
import com.example.remitlyintern.Exceptions.CountryISODoesNotExistException;
import com.example.remitlyintern.Exceptions.CountryISOSwiftCodeMismatchException;
import com.example.remitlyintern.Exceptions.HeadquarterSwiftCodeMismatchException;
import com.example.remitlyintern.Exceptions.SwiftCodeAlreadyExistInDataBaseException;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import org.springframework.stereotype.Component;

@Component
public class SwiftCodeValidator {
    /**
     * This class contains methods for validating the user's provided SwiftCode in a POST request.
     */
    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftCodeValidator(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    public void validateSwiftCodeForCreation(PostSwiftCodeDTO postSwiftCodeDTO) {
        validateCountryIsoExists(postSwiftCodeDTO.getCountryISO2());
        validateCountryIsoMatchesSwiftCode(postSwiftCodeDTO);
        validateHeadquarterConsistency(postSwiftCodeDTO);
        validateSwiftCodeUniqueness(postSwiftCodeDTO.getSwiftCode());
    }

    private void validateCountryIsoExists(String countryISO2) {
        if (!CountryISO2Map.countryIsoToCountryMap.containsKey(countryISO2)) {
            throw new CountryISODoesNotExistException("Provided CountryIso does not exist in a ISO standard");
        }
    }

    private void validateCountryIsoMatchesSwiftCode(PostSwiftCodeDTO postSwiftCodeDTO) {
        String countryISOFromSwiftCode = postSwiftCodeDTO.getSwiftCode().substring(4, 6);
        if (!postSwiftCodeDTO.getCountryISO2().equalsIgnoreCase(countryISOFromSwiftCode)) {
            throw new CountryISOSwiftCodeMismatchException("CountryISO code does not match with 5's and 6's letter from SwiftCode which are responsible for CountryISO");
        }
    }

    private void validateHeadquarterConsistency(PostSwiftCodeDTO postSwiftCodeDTO) {
        boolean endsWithXXX = postSwiftCodeDTO.getSwiftCode().endsWith("XXX");
        boolean isHeadquarter = postSwiftCodeDTO.Headquarter();

        if (endsWithXXX && !isHeadquarter) {
            throw new HeadquarterSwiftCodeMismatchException("Provided swiftCode suggests that it is a headquarter but user provided false in headquarter field");
        }
        if (!endsWithXXX && isHeadquarter) {
            throw new HeadquarterSwiftCodeMismatchException("If swiftCode does not end with XXX then it can not be a headquarter");
        }
    }

    private void validateSwiftCodeUniqueness(String swiftCode) {
        if (swiftCodeRepository.existsBySwiftCode(swiftCode)) {
            throw new SwiftCodeAlreadyExistInDataBaseException("Provided SwiftCode already exists in DataBase");
        }
    }
}
