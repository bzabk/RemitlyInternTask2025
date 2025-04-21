package com.example.remitlyintern.Repository;

import com.example.remitlyintern.Model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode,String> {
    /**
     * Repository interface for managing SwiftCode entities.
     * Extends JpaRepository to provide basic CRUD operations and custom query methods.
     */
    Optional<SwiftCode> findBySwiftCode(String swiftCode);

    boolean existsBySwiftCode(String swiftCode);

    List<SwiftCode> findAllByCountryISO2(String countryISO);

    void deleteSwiftCodeBySwiftCode(String swiftCode);

    List<SwiftCode> findAllBySwiftCodeStartingWithAndHeadquarterFalse(String mainPart);
}
