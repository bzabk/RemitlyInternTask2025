package com.example.remitlyintern.Service;

import com.example.remitlyintern.Dto.*;
import com.example.remitlyintern.Exceptions.*;
import com.example.remitlyintern.Model.SwiftCode;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import com.example.remitlyintern.Utils.CountryISO2Map;
import com.example.remitlyintern.Utils.SwiftCodeMapper;
import com.example.remitlyintern.Utils.SwiftCodeValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

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
         /**
         * Creates and saves a new SwiftCode record in the database.
         *
         * This method validates the provided SwiftCode data, maps it to an entity and
         * sets up relationships (e.g., headquarter and branches)
         *
         * @param postSwiftCodeDTO provided swiftCode data in post request
         * @return a success message indicating that the SwiftCode was saved
         * @throws ValidationException if the provided SwiftCode data is invalid
         */
         String returnInformation;

        swiftCodeValidator.validateSwiftCodeForCreation(postSwiftCodeDTO);
        String standardizedCountryName = CountryISO2Map.countryIsoToCountryMap.get(postSwiftCodeDTO.getCountryISO2());
        if(!Objects.equals(standardizedCountryName, postSwiftCodeDTO.getCountryName())){
            postSwiftCodeDTO.setCountryName(standardizedCountryName.toUpperCase());
            returnInformation = "SwiftCode successfully saved in database, CountryName was standardized to: "+standardizedCountryName.toUpperCase();
        }else{
            returnInformation = "SwiftCode successfully saved in database";
        }
        SwiftCode newSwiftCode = SwiftCodeMapper.toEntity(postSwiftCodeDTO);

        setupSwiftCodeRelationships(newSwiftCode);
        swiftCodeRepository.save(newSwiftCode);

        return returnInformation;
    }

    private void setupSwiftCodeRelationships(SwiftCode swiftCode) {
        /**
         * Method serves the purpose of handling the relationship between swift_code and swift_code_parent in the database
         * when a new SwiftCode is posted.
         */
        if (swiftCode.getHeadquarter()) {
            setupBranchesToHeadquarter(swiftCode);
        } else {
            assignToExistingHeadquarter(swiftCode);
        }
    }

    private void assignToExistingHeadquarter(SwiftCode branch) {
        /**
         * If a branch bank is posted, we check whether its headquarters exist in the database.
         * If so, we assign the branch to its headquarters.
         */
        String headquarterSwiftCode = branch.getSwiftCode().substring(0, 8) + "XXX";
        swiftCodeRepository.findBySwiftCode(headquarterSwiftCode)
                .ifPresent(branch::setParentSwiftCode);
    }

    private void setupBranchesToHeadquarter(SwiftCode headquarter) {
        /**
         * If a headquarters bank is posted, we check whether there are any branch banks connected to
         * the provided bank. If so, we update the `swift_code_parent` field in the branch banks.
         */
        String mainPart = headquarter.getSwiftCode().substring(0, 9);
        List<SwiftCode> branches = swiftCodeRepository.findAllBySwiftCodeStartingWithAndHeadquarterFalse(mainPart);
        branches.forEach(branch -> {
            branch.setParentSwiftCode(headquarter);
            swiftCodeRepository.save(branch);
        });
    }

    @Transactional
    public Object deleteRecordBySwiftCode(String swiftCode) {
        /**
         * Method deletes SwiftCode when @DeleteMapping("/{swift_code}") is executed
         * There is no need to set null in children's parent_swift_code @PreRemove in SwiftCode class handles it
         * @throws RecordNotFoundException when there is no record in database with provided SwiftCode
         *
         */
        swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new RecordNotFoundException("SWIFT code not found in the database"));

        swiftCodeRepository.deleteSwiftCodeBySwiftCode(swiftCode);
        return "Swift Code was deleted successfully";
    }

    public Object getSwiftCodeDetails(String swiftCode) {
        /**
         * Handles the retrieval of SWIFT code details via a GET request to the endpoint "/{swiftCode}".
         *
         * @param swiftCode the SWIFT code to retrieve details for
         * @return the details of the SWIFT code, either as a headquarters or branch DTO
         * @throws RecordNotFoundException if the provided SWIFT code does not exist in the database
         */
        SwiftCode foundSwiftCode = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new RecordNotFoundException("The provided SWIFT code does not exist in the database"));

        if (foundSwiftCode.getHeadquarter()) {
            return SwiftCodeMapper.toHeadquarterDTO(foundSwiftCode);
        } else {
            return SwiftCodeMapper.toBranchWithCountryName(foundSwiftCode);
        }
    }

    public CountryDetailsDTO getSwiftCodesByCountryISO2(String countryISO2) {
        /**
         * Handles the @GetMapping("/country/{countryISO2code}") request.
         * Retrieves all SWIFT codes for a given country based on its ISO2 code.
         */
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
