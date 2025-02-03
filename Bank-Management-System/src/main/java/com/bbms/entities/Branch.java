package com.bbms.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;

    @NotNull(message = "Branch name can not be null!")
    @NotEmpty(message = "Branch name can not be null!")
    private String branchName;

    @Size(min = 11, max = 11, message = "IFSC code must of 11 digits!")
    @NotNull(message = "IFSC code must not be null.")
    private String ifscCode;

    @ManyToOne
    @JoinColumn(name = "bankId")
    @JsonBackReference
    private Bank bank;

    private String branchAddress1;

    private String branchAddress2;

    private String branchCity;

    @Size(min = 9, max = 9, message = "MICR must be of length 9!")
    private String branch_MICR;

    private Long branchContact;

    @Email(message = "Must be a email!")
    private String branchEmail;

    private Long branchZip;

    public Branch() {
    }

    public Branch(Long branchId, String branchName, String IFSC_Code, Bank bank, String branchAddress1, String branchAddress2, String branchCity, String branch_MICR, Long branchContact, String branchEmail, Long branchZip) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.ifscCode=IFSC_Code;
        this.bank = bank;
        this.branchAddress1 = branchAddress1;
        this.branchAddress2 = branchAddress2;
        this.branchCity = branchCity;
        this.branch_MICR = branch_MICR;
        this.branchContact = branchContact;
        this.branchEmail = branchEmail;
        this.branchZip = branchZip;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public @Size(min = 11, max = 11, message = "IFSC code must of 11 digits!") @NotNull(message = "IFSC code must not be null.") String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(@Size(min = 11, max = 11, message = "IFSC code must of 11 digits!") @NotNull(message = "IFSC code must not be null.") String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getBranchAddress1() {
        return branchAddress1;
    }

    public void setBranchAddress1(String branchAddress1) {
        this.branchAddress1 = branchAddress1;
    }

    public String getBranchAddress2() {
        return branchAddress2;
    }

    public void setBranchAddress2(String branchAddress2) {
        this.branchAddress2 = branchAddress2;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(String branchCity) {
        this.branchCity = branchCity;
    }

    public String getBranch_MICR() {
        return branch_MICR;
    }

    public void setBranch_MICR(String branch_MICR) {
        this.branch_MICR = branch_MICR;
    }

    public Long getBranchContact() {
        return branchContact;
    }

    public void setBranchContact(Long branchContact) {
        this.branchContact = branchContact;
    }

    public String getBranchEmail() {
        return branchEmail;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

    public Long getBranchZip() {
        return branchZip;
    }

    public void setBranchZip(Long branchZip) {
        this.branchZip = branchZip;
    }

    @Override
    public String toString() {
        return "Branch {" +
                "branchId=" + branchId +
                ", branchName='" + branchName + '\'' +
                ", IFSC_Code='" + ifscCode + '\'' +
                ", bank=" + bank +
                ", branchAddress1='" + branchAddress1 + '\'' +
                ", branchAddress2='" + branchAddress2 + '\'' +
                ", branchCity='" + branchCity + '\'' +
                ", branch_MICR='" + branch_MICR + '\'' +
                ", branchContact=" + branchContact +
                ", branchEmail='" + branchEmail + '\'' +
                ", branchZip=" + branchZip +
                '}';
    }
}
