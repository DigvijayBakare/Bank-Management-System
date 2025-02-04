package com.bbms.service;

import com.bbms.entities.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BranchService {
    public Branch findBranchById(Long branchId);
    public Branch findBranchByName(String branchName);
    public List<Branch> findAllBranches();
    public Page<Branch> findAllBranchesUsingPages(Long bankId, Pageable page);
    public Branch saveBranch(Branch newBranch);
    public Branch updateBranch(Branch updateBranch,Long branchId);
    public void deleteBranch(Long branchId);
}
