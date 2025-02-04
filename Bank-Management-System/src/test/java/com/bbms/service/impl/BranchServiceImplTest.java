package com.bbms.service.impl;

import com.bbms.custom_exception.BranchNotFoundException;
import com.bbms.entities.Branch;
import com.bbms.repositories.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BranchServiceImplTest {
    @Mock
    private BranchRepository branchRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BranchServiceImpl branchService;

    List<Branch> branches;
    Page<Branch> branchPages;

    Optional<Branch> branch = Optional.of(new Branch(1L, "Branch1", "IFSCCO12345", null, "Address1",
            "Address2", "City1", "MICR1Micr", 9586741230L, "branch1@gmail.com", 456321L));

    private Long existingBranchId = 1L;
    private Long nonExistingBranchId = 10L;
    private Branch existingBranch;
    private Branch updateBranch;
    private Long bankId = 1L;
    private Pageable page;


    @BeforeEach
    void setUp() {
        this.branchService = new BranchServiceImpl(this.branchRepository, this.messageSource);
        branches = Arrays.asList(
                new Branch(1L, "Branch1", "IFSCCO12345", null, "Address1",
                        "Address2", "City1", "MICR1Micr", 9586741230L, "branch1@gmail.com", 456321L),
                new Branch(2L, "Branch2", "IFSCC112345", null, "Address2",
                        "Address2", "City2", "MICR1Micr", 9586741230L, "branch1@gmail.com", 456321L)
        );
        when(messageSource.getMessage(eq("ge.handleBranchWithIdNotFoundException"),
                any(), any())).thenReturn("Branch with id " + nonExistingBranchId + " does not exist!");

        when(branchRepository.findById(1L)).thenReturn(branch);
        when(branchRepository.findBranchByBranchName("Branch1")).thenReturn(branch);
        when(branchRepository.findAll()).thenReturn(branches);
        when(branchRepository.save(branch.get())).thenReturn(branch.get());

        // create an existing branch object
        existingBranch = new Branch(existingBranchId, "Old Branch", "IFSCCO12345", null, "Address1",
                "Address2", "City1", "MICR1Micr", 9586741230L, "oldbranch@gmail.com", 456321L);

        updateBranch = new Branch(existingBranchId, "Old Branch", "IFSCCO12345", null, "Address1",
                "Address2", "City1", "MICR1Micr", 9586741230L, "oldbranch@gmail.com", 456321L);

        when(branchService.findAllBranchesUsingPages(bankId,page)).thenReturn(branchPages);
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
        String nonExistingBranchName = "Branch21";
        when(this.messageSource.getMessage(eq("ge.handleBranchWithIdNotFoundException"),
                any(), any())).thenReturn("Branch with id " + nonExistingBranchName + " does not exist!");

        assertThrows(BranchNotFoundException.class,
                () -> branchService.findBranchByName(nonExistingBranchName));
    }

    @Test
    void findAllBranches() {
        List<Branch> allBranches = branchService.findAllBranches();
        assertEquals(branches, allBranches);
    }

    @Test
    void findAllBranchesNotFound() {
        when(branchService.findAllBranches()).thenReturn(new ArrayList<>());
        when(this.messageSource.getMessage(eq("ge.handleBranchNotFoundException"),
                any(), any())).thenReturn("Branch data does not exist!");

        assertThrows(BranchNotFoundException.class,
                () -> branchService.findAllBranches());
    }

    @Test
    void findBranchesWithBankIdAndPagination() {
        Page<Branch> allBranchesUsingPages = branchService.findAllBranchesUsingPages(bankId, page);
        assertEquals(branchPages,allBranchesUsingPages);
    }

    @Test
    void saveBranch() {
        Branch savedBranch = branchService.saveBranch(branch.get());
        assertEquals(branch.get(), savedBranch);
    }

    @Test
    void updateBranchIdIfExists() {
        when(branchRepository.findById(existingBranchId)).
                thenReturn(Optional.of(existingBranch));
        branchService.updateBranch(updateBranch, existingBranchId);

        Mockito.verify(branchRepository).save(existingBranch);
        assertEquals(updateBranch.getBranchName(), existingBranch.getBranchName());
        assertEquals(updateBranch.getIfscCode(), existingBranch.getIfscCode());
        assertEquals(updateBranch.getBranchCity(), existingBranch.getBranchCity());
        assertEquals(updateBranch.getBranchContact(), existingBranch.getBranchContact());
        assertEquals(updateBranch.getBranchEmail(), existingBranch.getBranchEmail());
        assertEquals(updateBranch.getBranchZip(), existingBranch.getBranchZip());
    }

    @Test
    void updateBranchIdIfNotExists() {
        when(branchRepository.findById(nonExistingBranchId))
                .thenReturn(Optional.empty());

        when(this.messageSource.getMessage(eq("ge.handleBranchWithIdNotFoundException"),
                any(), any())).thenReturn("Branch with id " + nonExistingBranchId + " does not exist!");

        assertThrows(BranchNotFoundException.class,
                () -> branchService.updateBranch(updateBranch, nonExistingBranchId));
    }

    @Test
    void deleteBranchWhenBankExists() {
        branchService.deleteBranch(existingBranchId);
    }

    @Test
    void deleteBranchWhenBankNotExists() {
        when(this.messageSource.getMessage(eq("ge.handleBranchWithIdNotFoundException"),
                any(), any())).thenReturn("Branch with id " + nonExistingBranchId + " does not exist!");

        assertThrows(BranchNotFoundException.class,
                () -> branchService.deleteBranch(nonExistingBranchId));
    }
}