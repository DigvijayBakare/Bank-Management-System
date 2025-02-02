package com.bbms.controller;

import com.bbms.entities.Bank;
import com.bbms.entities.Branch;
import com.bbms.repositories.BankRepository;
import com.bbms.repositories.BranchRepository;
import com.bbms.service.impl.BranchServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(BranchController.class)
class BranchControllerTest {
    @MockBean
    private BranchServiceImpl branchService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BindingResult result;

    @InjectMocks
    private BranchController branchController;

    @Autowired
    private ObjectMapper objectMapper;

//    @Mock
//    private BranchRepository branchRepository;

    Long id = 1L;
    String name = "Branch1";

    Optional<Branch> branchPersist = Optional.of(new Branch(1L, "Branch1", "IFSCCO12345", null,
            "Address1", "Address2", "City1", "MICR1Micr",
            9586741230L, "branch1@gmail.com", 456321L));

    @Test
    void saveBranch() throws Exception {
        when(branchService.saveBranch(any(Branch.class))).thenReturn(branchPersist.get());

        mockMvc.perform(post("/api/branch/add/branch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(branchPersist)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void saveBranchIfErrorInValidation() {
//        BindingResult result = Mockito.mock(BindingResult.class);
        List<ObjectError> errors = new ArrayList<>();
        ObjectError error = new ObjectError("branch", "Branch name must not be empty");
        errors.add(error);
        when(result.hasErrors()).thenReturn(true);
        when(result.getAllErrors()).thenReturn(errors);

        when(branchController.saveBranch(branchPersist.get(),result))
                .thenThrow(new ConstraintViolationException("Validation failed!",null,"Not Found!"));

        assertThrows(ConstraintViolationException.class,
                ()-> branchController.saveBranch(branchPersist.get(),result));
    }

    @Test
    void getBranchById() throws Exception {
        when(branchService.findBranchById(id)).thenReturn(branchPersist.get());

        mockMvc.perform(get("/api/branch/get/branch-id/{branchId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchId").value(id))
                .andExpect(jsonPath("$.branchName").value(branchPersist.get().getBranchName()))
                .andExpect(jsonPath("$.branchEmail").value(branchPersist.get().getBranchEmail()))
                .andExpect(jsonPath("$.branchCity").value(branchPersist.get().getBranchCity()))
                .andDo(result -> {
                    System.out.println(branchPersist);
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

   /* @Test
    void getBranchByIdNotFound() throws Exception {
        Long id1 = 10L;
        Optional<Branch> optionalBranch = Optional.empty();
//        when(branchService.findBranchById(id1)).thenReturn(optionalBranch.get());
        when(branchRepository.findById(id1)).thenReturn(optionalBranch);
        mockMvc.perform(get("/api/branch/get/branch-id/{branchId}", id1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }*/

    @Test
    void getBranchByName() throws Exception {
        when(branchService.findBranchByName(name)).thenReturn(branchPersist.get());
        mockMvc.perform(get("/api/branch/get/branch/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchId").value(id))
                .andExpect(jsonPath("$.branchName").value(branchPersist.get().getBranchName()))
                .andExpect(jsonPath("$.branchEmail").value(branchPersist.get().getBranchEmail()))
                .andExpect(jsonPath("$.branchCity").value(branchPersist.get().getBranchCity()))
                .andDo(result -> {
                    System.out.println(branchPersist);
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void getAllBranches() throws Exception {
        List<Branch> branchList = Arrays.asList(
                new Branch(1L, "Branch1", "IFSCCO12345", null,
                        "Address1", "Address2", "City1", "MICR1Micr",
                        9586741230L, "branch1@gmail.com", 456321L),
                new Branch(2L, "Branch2", "IFSCCO12345", null,
                        "Address1", "Address2", "City1", "MICR1Micr",
                        9586741230L, "branch2@gmail.com", 458521L)
        );

        when(branchService.findAllBranches()).thenReturn(branchList);
        mockMvc.perform(get("/api/branch/get"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.size()").value(branchList.size()))
                .andDo(print());
    }

    @Test
    void updateBranchById() throws Exception {
        Branch oldBranch = branchPersist.get();
        Branch updatedBranch = new Branch(id, "Branch2", "IFSCCO12345", null,
                "Address1", "Address2", "City1", "MICR1Micr",
                9586741230L, "branch2@gmail.com", 458521L);

        when(branchService.findBranchById(id)).thenReturn(branchPersist.get());
        when(branchService.updateBranch(updatedBranch,id)).thenReturn(oldBranch);

        mockMvc.perform(put("/api/branch/update/branch/{branchId}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBranch)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void updateBranchIfErrorInValidation() {
        List<ObjectError> errors = new ArrayList<>();
        ObjectError error = new ObjectError("branch", "Invalid branch updates!");
        errors.add(error);
        when(result.hasErrors()).thenReturn(true);
        when(result.getAllErrors()).thenReturn(errors);

        when(branchController.updateBranchById(id,branchPersist.get(),result))
                .thenThrow(new ConstraintViolationException("Validation failed!",null,"Not Found!"));

        assertThrows(ConstraintViolationException.class,
                ()-> branchController.updateBranchById(id, branchPersist.get(),result));
    }

    @Test
    void deleteBranch() throws Exception {
        doNothing().when(branchService).deleteBranch(id);
        mockMvc.perform(delete("/api/branch/delete/branch/{branchId}",id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}