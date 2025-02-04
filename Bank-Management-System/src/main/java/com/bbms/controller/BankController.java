package com.bbms.controller;

import com.bbms.entities.Bank;
import com.bbms.service.impl.BankServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    private static Logger log = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private BankServiceImpl bankService;

    public BankController(BankServiceImpl bankService) {
        this.bankService = bankService;
    }

    // handler method to save the new bank
    @PostMapping("/add/bank")
    public ResponseEntity<?> saveBank(@Valid @RequestBody Bank bank, BindingResult result) {
        if (result.hasErrors()) {
            log.error("error while saving bank details: {}", result.getAllErrors());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occurred while saving the bank details!\n"+result.getAllErrors());
        }
        Bank saveBank = this.bankService.saveBank(bank);
        log.info("successfully created bank object: {}" , saveBank);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveBank);
    }
    // get all branch
    @GetMapping("/get/bank")
    public ResponseEntity<?> getAllBanks(){
        List<Bank> allBanks = this.bankService.findAllBanks();
        if(allBanks.isEmpty()) {
            log.error("No bank details found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bank details are present!");
        }
        log.info("all bank details has been successfully fetched!");
        return ResponseEntity.status(HttpStatus.FOUND).body(allBanks);
    }

    // get all banks with pages only
    @GetMapping("/get/bank/page")
    public ResponseEntity<?> getAllBanksPages(Pageable page){
        Page<Bank> banks = this.bankService.findAllBanksPage(page);

        if(banks.isEmpty()) {
            log.debug("No bank details found on desired page!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bank details are present on given page number!");
        }

        log.info("all bank details on given page has been successfully fetched!");
        return ResponseEntity.status(HttpStatus.FOUND).body(banks);
    }

    // get single branch by id
    @GetMapping("/get/bank/id/{bankId}")
    public ResponseEntity<?> getSingleBank(@PathVariable("bankId")Long bankId) {
        Bank bank = this.bankService.findBankById(bankId);
        log.info("bank with id: {} is found and returned!!", bankId);
        return ResponseEntity.status(HttpStatus.FOUND).body(bank);
    }

    // get single branch by name
    @GetMapping("/get/bank/{bankName}")
    public ResponseEntity<?> getSingleBank(@PathVariable("bankName")String bankName) {
        Bank bank = this.bankService.findBankByName(bankName);
        log.info("bank with name: {} is found and returned!!", bankName);
        return ResponseEntity.status(HttpStatus.FOUND).body(bank);
    }

    // update a bank using bank id
    @PutMapping("/update/bank/{bankId}")
    public ResponseEntity<?> updateBank(@PathVariable("bankId")long bankId, @Valid @RequestBody Bank bank, BindingResult result){
        if (result.hasErrors()) {
            log.error("an error occurred while updating a bank details");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.getAllErrors());
        }
        this.bankService.updateBank(bankId,bank);
        log.info("bank details successfully updated!");
        return ResponseEntity.ok().body("bank with id: " +  bankId + " updated successfully!");
    }

    // delete a bank using bank id
    @DeleteMapping("/delete/bank/{bankId}")
    public ResponseEntity<?> deleteBank(@PathVariable("bankId")Long bankId) {
        this.bankService.deleteBank(bankId);
        log.info("bank with id: {} deleted successfully", bankId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Bank deleted successfully!");
    }
}
