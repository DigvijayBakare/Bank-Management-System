package com.bbms.service.impl;

import com.bbms.custom_exception.BranchNotFoundException;
import com.bbms.entities.Branch;
import com.bbms.repositories.BranchRepository;
import com.bbms.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BranchServiceImpl implements BranchService {

    private static final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    @Autowired
    private BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public Branch saveBranch(Branch newBranch) {
        return this.branchRepository.save(newBranch);
    }


    @Override
    public Branch findBranchById(Long branchId) {
        return this.branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch with id " + branchId + " does not exists!"));

    }

    @Override
    public Branch findBranchByName(String branchName) {
        return this.branchRepository.findBranchByBranchName(branchName)
                .orElseThrow(() -> new BranchNotFoundException("Branch with name " + branchName + " does not exists!"));
    }

    @Override
    public List<Branch> findAllBranches() {
        logger.info("fetching from the database!");
        List<Branch> all = this.branchRepository.findAll();
        if (all.isEmpty()) {
            logger.warn("no branch found!");
            throw new BranchNotFoundException("no branch found ");
        } else {
            logger.info("branches fetched successfully Total: {}", all.size());
            return all;
        }
    }

    @Override
    public Page<Branch> findAllBranchesUsingPages(int page) {
        // pagination implementation - per page 10(n) banks and current page 0(page) - start page no is 0
        Pageable pageable = PageRequest.of(page, 10);
        return this.branchRepository.findAll(pageable);
    }


    @Override
    public Branch updateBranch(Branch updateBranch, Long branchId) {
        Optional<Branch> byId = this.branchRepository.findById(branchId);
        if (byId.isEmpty()) {
            throw new BranchNotFoundException("branch with id " + branchId + " is not present.");
        } else {
            Branch branch = byId.get();
            branch.setBranchId(branchId);
            branch.setBranchName(updateBranch.getBranchName());
            branch.setIfscCode(updateBranch.getIfscCode());
            branch.setBranchZip(updateBranch.getBranchZip());
            branch.setBranchContact(updateBranch.getBranchContact());
            branch.setBranchEmail(updateBranch.getBranchEmail());
            branch.setBranchAddress1(updateBranch.getBranchAddress1());
            branch.setBranchAddress2(updateBranch.getBranchAddress2());
            branch.setBranch_MICR(updateBranch.getBranch_MICR());
            branch.setBank(updateBranch.getBank());
            branch.setBranchCity(updateBranch.getBranchCity());
            return this.branchRepository.save(branch);
        }
    }

    @Override
    public void deleteBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId).
                orElseThrow(() -> new BranchNotFoundException("Branch with id " + branchId + " is not present in the record."));
        this.branchRepository.delete(branch);
    }
}
