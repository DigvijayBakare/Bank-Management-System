package com.bbms.service.impl;

import com.bbms.custom_exception.BranchNotFoundException;
import com.bbms.entities.Branch;
import com.bbms.repositories.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BranchServiceImplTest {
    @Autowired
    private BranchServiceImpl branchService;

    @Mock
    private BranchRepository branchRepository;

    List<Branch> branches;

    Optional<Branch> branch = Optional.of(new Branch(1L, "Branch1", "IFSCCO12345", null, "Address1",
            "Address2", "City1", "MICR1Micr", 9586741230L, "branch1@gmail.com", 456321L));

    private Long existingBranhId = 1L;
    private Long nonExistingBranhId = 5L;
    private Branch existingBranch;
    private Branch updateBranch;


    @BeforeEach
    void setUp() {
        this.branchService = new BranchServiceImpl(this.branchRepository);
        branches = Arrays.asList(
                new Branch(1L, "Branch1", "IFSCCO12345", null, "Address1",
                        "Address2", "City1", "MICR1Micr", 9586741230L, "branch1@gmail.com", 456321L),
                new Branch(2L, "Branch2", "IFSCC112345", null, "Address2",
                        "Address2", "City2", "MICR1Micr", 9586741230L, "branch1@gmail.com", 456321L)
        );

        Mockito.when(branchRepository.findById(1L)).thenReturn(branch);
        Mockito.when(branchRepository.findBranchByBranchName("Branch1")).thenReturn(branch);
        Mockito.when(branchRepository.findAll()).thenReturn(branches);
        Mockito.when(branchRepository.save(branch.get())).thenReturn(branch.get());

        // create an existing branch object
        existingBranch = new Branch(existingBranhId, "Old Branch", "IFSCCO12345", null, "Address1",
                "Address2", "City1", "MICR1Micr", 9586741230L, "oldbranch@gmail.com", 456321L);

        updateBranch = new Branch(existingBranhId, "Old Branch", "IFSCCO12345", null, "Address1",
                "Address2", "City1", "MICR1Micr", 9586741230L, "oldbranch@gmail.com", 456321L);
    }

    @Test
    void findBranchByIdExists() {
        Branch branchById = branchService.findBranchById(1L);
        assertEquals(branch.get(), branchById);
    }

    @Test
    void findBranchByIdNotExists() {
        assertThrows(BranchNotFoundException.class,
                () -> branchService.findBranchById(10L));
    }

    @Test
    void findBranchByNameExists() {
        Branch branchById = branchService.findBranchByName("Branch1");
        assertEquals(branch.get(), branchById);
    }

    @Test
    void findBranchByNameNotExists() {
        assertThrows(BranchNotFoundException.class,
                () -> branchService.findBranchByName("Branch21"));
    }

    @Test
    void findAllBranches() {
        List<Branch> allBranches = branchService.findAllBranches();
        assertEquals(branches, allBranches);
    }

    @Test
    void findAllBranchesNotFound() {
        Mockito.when(branchService.findAllBranches()).thenReturn(new ArrayList<>());
        assertThrows(BranchNotFoundException.class,
                ()-> branchService.findAllBranches());
    }

    @Test
    void saveBranch() {
        Branch savedBranch = branchService.saveBranch(branch.get());
        assertEquals(branch.get(), savedBranch);
    }

    @Test
    void updateBranchIdIfExists() {
        Mockito.when(branchRepository.findById(existingBranhId)).thenReturn(Optional.of(existingBranch));
        branchService.updateBranch(updateBranch, existingBranhId);

        Mockito.verify(branchRepository).save(existingBranch);
        assertEquals(updateBranch.getBranchName(),existingBranch.getBranchName());
        assertEquals(updateBranch.getIfscCode(),existingBranch.getIfscCode());
        assertEquals(updateBranch.getBranchCity(),existingBranch.getBranchCity());
        assertEquals(updateBranch.getBranchContact(),existingBranch.getBranchContact());
        assertEquals(updateBranch.getBranchEmail(),existingBranch.getBranchEmail());
        assertEquals(updateBranch.getBranchZip(),existingBranch.getBranchZip());
    }

    @Test
    void updateBranchIdIfNotExists() {
        Mockito.when(branchRepository.findById(nonExistingBranhId)).thenReturn(Optional.empty());
        assertThrows(BranchNotFoundException.class,
                () -> branchService.updateBranch(updateBranch, nonExistingBranhId));
    }

    @Test
    void deleteBranchWhenBankExists() {
        branchService.deleteBranch(existingBranhId);
    }

    @Test
    void deleteBranchWhenBankNotExists() {
        assertThrows(BranchNotFoundException.class,
                () -> branchService.deleteBranch(nonExistingBranhId));
    }
}