package com.example.remitlyintern.Repository;

import com.example.remitlyintern.Model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode,String> {

    Optional<SwiftCode> findBySwiftCode(String swiftCode);

    List<SwiftCode> findAllByCountryIso2(String countryISO);
}
