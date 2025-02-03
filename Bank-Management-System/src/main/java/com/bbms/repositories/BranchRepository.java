package com.bbms.repositories;

import com.bbms.entities.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    public Optional<Branch> findBranchByBranchName(String branchName);

    // pagination for later uses
    @Query("from Branch as b where b.bank.bankId =:bankId")
    public Page<Branch> findBranchByBankId(@Param("bankId")Long bankId, Pageable pageable);
}
