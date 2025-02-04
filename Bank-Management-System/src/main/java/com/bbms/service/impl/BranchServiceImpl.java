package com.bbms.service.impl;

import com.bbms.custom_exception.BranchNotFoundException;
import com.bbms.entities.Branch;
import com.bbms.repositories.BranchRepository;
import com.bbms.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BranchServiceImpl implements BranchService {
    private static final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository, MessageSource messageSource) {
        this.branchRepository = branchRepository;
        this.messageSource = messageSource;
    }

    @Override
    public Branch saveBranch(Branch newBranch) {
        return this.branchRepository.save(newBranch);
    }


    @Override
    public Branch findBranchById(Long branchId) {
        return this.branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException(
                        messageSource.getMessage("ge.handleBranchWithIdNotFoundException",
                                new Object[]{branchId}, LocaleContextHolder.getLocale())));
    }


    @Override
    public Branch findBranchByName(String branchName) {
        return this.branchRepository.findBranchByBranchName(branchName)
                .orElseThrow(() -> new BranchNotFoundException(
                        messageSource.getMessage("ge.handleBranchWithNameNotFoundException",
                                new Object[]{branchName}, LocaleContextHolder.getLocale())));
    }

    @Override
    public List<Branch> findAllBranches() {
        logger.info("fetching from the database!");
        List<Branch> all = this.branchRepository.findAll();
        if (all.isEmpty()) {
            logger.warn("No branches found!");
            throw new BranchNotFoundException(
                    messageSource.getMessage("ge.handleBranchNotFoundException",
                            null, LocaleContextHolder.getLocale()));
        } else {
            logger.info("branches fetched successfully Total: {}", all.size());
            return all;
        }
    }

    @Override
    public Page<Branch> findAllBranchesUsingPages(Long bankId, Pageable page) {
        // pagination implementation - per page 10(n) banks and current page 0(page) - start page no is 0
        return this.branchRepository.findByBankBankId(bankId, page);
    }


    @Override
    public Branch updateBranch(Branch updateBranch, Long branchId) {
        Optional<Branch> byId = this.branchRepository.findById(branchId);
        if (byId.isEmpty()) {
            throw new BranchNotFoundException(
                    messageSource.getMessage("ge.handleBranchWithIdNotFoundException",
                            new Object[]{branchId}, LocaleContextHolder.getLocale()));
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
                orElseThrow(() -> new BranchNotFoundException(
                        messageSource.getMessage("ge.handleBranchWithIdNotFoundException",
                                new Object[]{branchId}, LocaleContextHolder.getLocale())));
        this.branchRepository.delete(branch);
    }
}
