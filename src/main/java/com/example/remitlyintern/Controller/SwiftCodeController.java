package com.example.remitlyintern.Controller;

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


@RestController
@RequestMapping("/v1/swift-codes")
@Validated
public class SwiftCodeController {

    @Autowired
    private  SwiftCodeService swiftCodeService;


    @GetMapping("/{swiftCode}")
    public ResponseEntity<Object> findSwiftCodeDetails(@PathVariable
                                                      @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$",
                                                              message = "Input does not satisfy required swift code regex")  String swiftCode){
        return ResponseEntity.ok(swiftCodeService.getSwiftCodeDetails(swiftCode));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<Object> findSwiftCodesByCountryISO(@PathVariable
                                                                 @Pattern(regexp = "^[A-Z]{2}$", message = "Country ISO code must be exactly two uppercase letters")
                                                                 String countryISO2code){
        return ResponseEntity.ok(swiftCodeService.getSwiftCodesByCountryISO2(countryISO2code));
    }

    @DeleteMapping("/{swift_code}")
    public ResponseEntity<Object> deleteBySwiftCode(@PathVariable
                                                    @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$",
                                                            message = "Input does not satisfy required swift code regex") String swift_code){
        return ResponseEntity.ok(swiftCodeService.deleteRecordBySwiftCode(swift_code));
    }












}
