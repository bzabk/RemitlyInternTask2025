package com.example.remitlyintern.Controller;

import com.example.remitlyintern.Dto.BranchDTO;
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

    private final SwiftCodeRepository swiftCodeRepository;
    private final SwiftCodeService swiftCodeService;

    @Autowired
    public SwiftCodeController(SwiftCodeRepository swiftCodeRepository, SwiftCodeService swiftCodeService) {
        this.swiftCodeRepository = swiftCodeRepository;
        this.swiftCodeService = swiftCodeService;
    }



    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> findSwiftCodeDetails(@PathVariable
                                                      @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$", message = "Input must contain only uppercase letters and digits in the SWIFT code format")  String swiftCode) {

        SwiftCode foundSwiftCode = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new NotFoundElementException("The provided SWIFT code does not exist in the database"));
        if(foundSwiftCode.isHeadquarter()){
            HeadquarterDTO headquarterDTO = swiftCodeService.convertToHeadquaterDTO(foundSwiftCode);
            headquarterDTO.setBranches(swiftCodeService.convertToBranchDTO(foundSwiftCode.getChildren()));
            return ResponseEntity.ok(headquarterDTO);
        }else{
            BranchDTO branchDTO = swiftCodeService.convertToBranchWithCountryName(foundSwiftCode);
            return ResponseEntity.ok(branchDTO);
        }


    }




}
