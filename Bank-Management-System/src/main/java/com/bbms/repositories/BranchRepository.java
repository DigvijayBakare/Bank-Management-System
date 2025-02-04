package com.bbms.repositories;

import com.bbms.entities.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    public Optional<Branch> findBranchByBranchName(String branchName);

    // pagination: branches by bank ID
    Page<Branch> findByBankBankId(Long bankId, Pageable pageable);
}
