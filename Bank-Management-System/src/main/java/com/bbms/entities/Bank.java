package com.bbms.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankId;

    @NotNull(message = "Bank name can not be null!")
    private String bankName;

    private Date bankEstablishedOn;

    @Size(min = 10, max = 10, message = "Bank contact number must be of length 10!")
    private String bankContact;

    @Email(message = "Bank email must be a valid email!")
    private String bankEmail;

    private boolean isActive = true;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Branch> branches;

    public Bank() {
    }

    public Bank(Long bankId, String bankName, Date bankEstablishedOn, String bankContact, String bankEmail, boolean isActive, List<Branch> branches) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankEstablishedOn = bankEstablishedOn;
        this.bankContact = bankContact;
        this.bankEmail = bankEmail;
        this.isActive = isActive;
        this.branches = branches;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Date getBankEstablishedOn() {
        return bankEstablishedOn;
    }

    public void setBankEstablishedOn(Date bankEstablishedOn) {
        this.bankEstablishedOn = bankEstablishedOn;
    }

    public String getBankContact() {
        return bankContact;
    }

    public void setBankContact(String bankContact) {
        this.bankContact = bankContact;
    }

    public String getBankEmail() {
        return bankEmail;
    }

    public void setBankEmail(String bankEmail) {
        this.bankEmail = bankEmail;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankId=" + bankId +
                ", bankName='" + bankName + '\'' +
                ", bankEstablishedOn=" + bankEstablishedOn +
                ", bankContact='" + bankContact + '\'' +
                ", bankEmail='" + bankEmail + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
