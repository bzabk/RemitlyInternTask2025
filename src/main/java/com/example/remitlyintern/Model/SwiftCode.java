package com.example.remitlyintern.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "swift_codes_mod")
public class SwiftCode {

    @Id
    @Column(name="swift_code",unique = true, length=11)
    private String swiftCode;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name="address")
    private String address;

    @Column(name="town_name")
    private String townName;

    @Column(name="country_iso2",length=2,nullable = false)
    @JsonProperty("countryISO2")
    private String countryIso2;

    @Column(name="country_name")
    private String countryName;

    @Column(name = "time_zone", nullable = false)
    private String timeZone;

    @Column(name="is_headquarter")
    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter = false;


    @ManyToOne
    @JoinColumn(name = "parent_swift_code",referencedColumnName = "swift_code",
    foreignKey = @ForeignKey(name="fkParentSwiftCode"))
    @JsonBackReference
    private SwiftCode parentSwiftCode;

    @OneToMany(mappedBy = "parentSwiftCode",
    cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<SwiftCode> children = new ArrayList<>();


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

    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryIso2() {
        return countryIso2;
    }

    public void setCountryIso2(String countryIso2) {
        this.countryIso2 = countryIso2;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
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
