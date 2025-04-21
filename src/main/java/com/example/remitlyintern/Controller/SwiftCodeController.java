package com.example.remitlyintern.Controller;
import com.example.remitlyintern.Dto.PostSwiftCodeDTO;
import com.example.remitlyintern.Service.SwiftCodeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/swift-codes")
@Validated
public class SwiftCodeController {

    /**
     *  @GetMapping("/{swiftCode}") - Retrieves detailed information about the provided SWIFT code, including:
     *  - address
     *  - bankName
     *  - countryISO2
     *  - countryName
     *  - isHeadquarter
     *  - swiftCode
     *  If the SWIFT code ends with "XXX" (indicating it is a headquarter), information about its branches is also included.
     *
     *  @GetMapping("/country/{countryISO2code}") - Returns all SWIFT codes associated with the specified country ISO2 code.
     *
     *  @DeleteMapping("/{swift_code}") - Deletes a record from the database based on the provided SWIFT code.
     *  If the SWIFT code ends with "XXX", the `parent_swift_code` of its children is set to null.
     *
     *  @PostMapping - Adds a new SWIFT code to the database. The request body should include the following fields:
     *  {
     *     "address": "TEST STREET",
     *     "bankName": "TEST NAME",
     *     "countryISO2": "TEST ISO2",
     *     "countryName": "TEST COUNTRY NAME",
     *     "isHeadquarter": false,
     *     "swiftCode": "TEST SWIFTCODE"
     *  }
     *  Fields `countryISO2` and `swiftCode` are validated using regular expressions.
     *  The provided `countryISO2` is also checked to ensure it matches a key in the `CountryISO2Map`.
     *
     */

    private final SwiftCodeService swiftCodeService;

    public SwiftCodeController(SwiftCodeService swiftCodeService) {
        this.swiftCodeService = swiftCodeService;
    }


    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> findSwiftCodeDetails(@PathVariable
                                                      @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$",
                                                              message = "Input does not satisfy required swift code regex")  String swiftCode){
        return ResponseEntity.ok(swiftCodeService.getSwiftCodeDetails(swiftCode));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<?> findSwiftCodesByCountryISO(@PathVariable
                                                                 @Pattern(regexp = "^[A-Z]{2}$", message = "Country ISO code must be exactly two uppercase letters")
                                                                 String countryISO2code){
        return ResponseEntity.ok(swiftCodeService.getSwiftCodesByCountryISO2(countryISO2code));
    }

    @DeleteMapping("/{swift_code}")
    public ResponseEntity<?> deleteBySwiftCode(@PathVariable
                                                    @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$",
                                                            message = "Input does not satisfy required swift code regex") String swift_code){
        return ResponseEntity.ok(swiftCodeService.deleteRecordBySwiftCode(swift_code));
    }


    @PostMapping
    public ResponseEntity<?> postNewSwiftCode(@RequestBody @Valid PostSwiftCodeDTO postSwiftCodeDTO){
        return ResponseEntity.ok(swiftCodeService.postNewSwiftCodeRecord(postSwiftCodeDTO));
    }

}
