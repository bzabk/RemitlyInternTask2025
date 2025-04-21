package com.example.remitlyintern.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "swift_codes_db")
public class SwiftCode {
    /**
     * Represents the SwiftCode entity, which stores information about bank SWIFT codes.
     * This class is mapped to the `swift_codes_db` table in the database and contains
     * details such as the SWIFT code, bank name, address, country, and whether it is a headquarters.
     * It also defines relationships with other SWIFT codes (e.g., parent-child relationships).
     *
     * The field representing whether a bank is a headquarters is named `headquarter` instead of `isHeadquarter`
     * to avoid potential issues with displaying the property in the response body as `isHeadquarter`.
     */
    @Id
    @Column(name="swift_code",unique = true, length=11)
    private String swiftCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name="address")
    private String address;


    @Column(name="country_iso2",length=2,nullable = false)
    @JsonProperty("countryISO2")
    private String countryISO2;

    @Column(name="country_name")
    private String countryName;

    @Column(name="is_headquarter")
    @JsonProperty("isHeadquarter")
    private boolean headquarter = false;


    @ManyToOne
    @JoinColumn(name = "parent_swift_code",referencedColumnName = "swift_code",
    foreignKey = @ForeignKey(name="fkParentSwiftCode"))
    @JsonBackReference
    private SwiftCode parentSwiftCode;

    @OneToMany(mappedBy = "parentSwiftCode",
    cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = false)
    @JsonManagedReference
    private List<SwiftCode> children = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        for (SwiftCode child : children) {
            child.setParentSwiftCode(null);
        }
    }

    public SwiftCode(String swiftCode, String bankName, String address, String countryISO2, String countryName, boolean isHeadquarter, SwiftCode parentSwiftCode, List<SwiftCode> children) {
        this.swiftCode = swiftCode;
        this.bankName = bankName;
        this.address = address;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.headquarter = isHeadquarter;
        this.parentSwiftCode = parentSwiftCode;
        this.children = children;
    }

    public SwiftCode() {
    }

    public List<SwiftCode> getChildren() {
        return children;
    }

    public void setChildren(List<SwiftCode> children) {
        this.children = children;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public SwiftCode getParentSwiftCode() {
        return parentSwiftCode;
    }

    public void setParentSwiftCode(SwiftCode parentSwiftCode) {
        this.parentSwiftCode = parentSwiftCode;
    }

    public boolean getHeadquarter() {
        return headquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        this.headquarter = headquarter;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(String countryIso2) {
        this.countryISO2 = countryIso2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
