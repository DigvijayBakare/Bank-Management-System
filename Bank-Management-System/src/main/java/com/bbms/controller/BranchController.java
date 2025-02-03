package com.bbms.controller;

import com.bbms.custom_exception.BranchNotFoundException;
import com.bbms.entities.Branch;
import com.bbms.service.impl.BankServiceImpl;
import com.bbms.service.impl.BranchServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);

    @Autowired
    private BranchServiceImpl branchServiceImp;

//    @Autowired
//    private BankServiceImpl bankService;

    // handler method for saving the new branch
    @PostMapping("/add/branch")
    public ResponseEntity<?> saveBranch(@Valid @RequestBody Branch branch, BindingResult bindingResult) {
        // check for validations if not satisfied
        if(bindingResult.hasErrors()){
            logger.warn("errors while saving the branch: {}", bindingResult.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        Branch saveBranch = this.branchServiceImp.saveBranch(branch);
        logger.info("branch added successfully: {}",saveBranch);
        return ResponseEntity.status(HttpStatus.CREATED).body("new branch created successfully\n" + saveBranch);
    }

    // get branch by id
    @GetMapping("/get/branch-id/{branchId}")
    public ResponseEntity<?> getBranchById(@PathVariable("branchId") Long branchId) {
        Branch branchById = this.branchServiceImp.findBranchById(branchId);

        logger.info("Fetched branch: {}", branchById);
        return ResponseEntity.ok(branchById);

    }

    // get branch by name
    @GetMapping("/get/branch/{name}")
    public ResponseEntity<?> getBranchByName(@Valid @PathVariable("name") String branchName) {
        Branch branchByName = this.branchServiceImp.findBranchByName(branchName);
        logger.info("Fetched branch: {}", branchByName);
        return ResponseEntity.ok(branchByName);
    }

    // get all branch
    @GetMapping("/get")
    public ResponseEntity<?> getAllBranches() {
        List<Branch> allBranches = this.branchServiceImp.findAllBranches();
        logger.info("fetched all branches successfully: {}", allBranches);
        return ResponseEntity.status(HttpStatus.FOUND).body(allBranches);
    }

    // get all branch according to the pagination
    @GetMapping("/get/page/{pageNo}")
    public ResponseEntity<?> getAllBranchesUsingPages(@PathVariable("pageNo") int pageNo) {
        Page<Branch> pageBranches = this.branchServiceImp.findAllBranchesUsingPages(pageNo);

        if (pageBranches.isEmpty()){
            logger.info("No data found on the given page!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data does not exists on the given page no!");
        }

        logger.info("fetched branches on the given page successfully: {}", pageBranches);
        return ResponseEntity.status(HttpStatus.FOUND).body(pageBranches);
    }

    // update branch
    @PutMapping("/update/branch/{branchId}")
    public ResponseEntity<?> updateBranchById(@Valid @PathVariable("branchId")Long branchId, @RequestBody Branch branch,BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            logger.warn("errors while updating the branch: {}", bindingResult.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please enter valid data .\nErrors while saving branch:\n"+bindingResult.getAllErrors());
        }
        Branch branch1 = this.branchServiceImp.updateBranch(branch, branchId);
        logger.info("branch updated successfully.{}",branch1);
        return ResponseEntity.status(HttpStatus.CREATED).body(branch1);
    }

    // handler for delete the branch.
    @DeleteMapping("/delete/branch/{branchId}")
    public ResponseEntity<String> deleteBranch(@PathVariable("branchId") Long branchId) {
        this.branchServiceImp.deleteBranch(branchId);
        logger.info("branch with id: {} deleted successfully!", branchId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("branch with id " + branchId + " is deleted successfully!");
    }
}