package com.bbms.repositories;

import com.bbms.entities.Branch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BranchRepositoryTest {
    @Autowired
    BranchRepository branchRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Branch branchPersist = new Branch(1L, "Branch1", "IFSCCO12345", null,
            "Address1", "Address2", "City1", "MICR1Micr",
            9586741230L, "branch1@gmail.com", 456321L);

    @BeforeEach
    void setUp() {
        testEntityManager.merge(branchPersist);
    }

    @Test
    void findBranchByBranchName() {
        Branch branch = branchRepository.findBranchByBranchName("Branch1").get();
        assertEquals(branchPersist.getBranchId(), branch.getBranchId());
        assertEquals(branchPersist.getBranchName(), branch.getBranchName());
        assertEquals(branchPersist.getIfscCode(), branch.getIfscCode());
        assertEquals(branchPersist.getBranchAddress1(), branch.getBranchAddress1());
        assertEquals(branchPersist.getBranchCity(), branch.getBranchCity());
        assertEquals(branchPersist.getBranch_MICR(), branch.getBranch_MICR());
        assertEquals(branchPersist.getBranchContact(), branch.getBranchContact());
        assertEquals(branchPersist.getBranchEmail(), branch.getBranchEmail());
        assertEquals(branchPersist.getBranchZip(), branch.getBranchZip());
    }
}