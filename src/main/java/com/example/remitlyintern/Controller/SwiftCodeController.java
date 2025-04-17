package com.example.remitlyintern.Controller;

import com.example.remitlyintern.Dto.BranchDTO;
import com.example.remitlyintern.Dto.BranchWithCountryName;
import com.example.remitlyintern.Dto.CountryDetailsDTO;
import com.example.remitlyintern.Dto.HeadquarterDTO;
import com.example.remitlyintern.Exceptions.NotFoundElementException;
import com.example.remitlyintern.Model.SwiftCode;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import com.example.remitlyintern.Service.SwiftCodeService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes")
@Validated
public class SwiftCodeController {

    private final SwiftCodeRepository swiftCodeRepository;
    private final SwiftCodeService swiftCodeService;

    @Autowired
    public SwiftCodeController(SwiftCodeRepository swiftCodeRepository, SwiftCodeService swiftCodeService) {
        this.swiftCodeRepository = swiftCodeRepository;
        this.swiftCodeService = swiftCodeService;
    }



    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> findSwiftCodeDetails(@PathVariable
                                                      @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$",
                                                              message = "Input does not satisfy required swift code regex")  String swiftCode) {

        SwiftCode foundSwiftCode = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new NotFoundElementException("The provided SWIFT code does not exist in the database"));

        if(foundSwiftCode.getHeadquarter()){
            HeadquarterDTO headquarterDTO = swiftCodeService.convertToHeadquaterDTO(foundSwiftCode);
            headquarterDTO.setBranches(swiftCodeService.convertToBranchDTO(foundSwiftCode.getChildren()));
            return ResponseEntity.ok(headquarterDTO);
        }else{
            BranchWithCountryName branchDTO = swiftCodeService.convertSwiftCodeToBranchWithCountryName(foundSwiftCode);
            return ResponseEntity.ok(branchDTO);
        }
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<Object> findSwiftCodesByCountryISO(@PathVariable
                                                                 @Pattern(regexp = "^[A-Z]{2}$", message = "Country ISO code must be exactly two uppercase letters")
                                                                 String countryISO2code){
        List<SwiftCode> listOfSwiftCodes = swiftCodeRepository.findAllByCountryIso2(countryISO2code);

        if(listOfSwiftCodes.isEmpty()){
            throw new NotFoundElementException("No Banks with entered countryISO in database");
        }
        CountryDetailsDTO countryDetailsDTO = new CountryDetailsDTO(countryISO2code,
                listOfSwiftCodes.stream()
                        .findFirst()
                        .map(SwiftCode::getCountryName)
                        .orElse(""));

        listOfSwiftCodes.forEach(swiftCode -> {
                        countryDetailsDTO.getBranchDTOList().add(swiftCodeService.convertSwiftCodeToBranchDTO(swiftCode));
                });
        return ResponseEntity.ok(countryDetailsDTO);
    }








}
