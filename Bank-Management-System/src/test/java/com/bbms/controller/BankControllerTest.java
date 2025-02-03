package com.bbms.controller;

import com.bbms.entities.Bank;
import com.bbms.service.impl.BankServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(BankController.class)
class BankControllerTest {
    @MockBean
    private BankServiceImpl bankService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BindingResult result;

    @InjectMocks
    private BankController bankController;

    @Autowired
    private ObjectMapper objectMapper;

    Long id = 1L;
    String name = "Bank1";

    Optional<Bank> bank = Optional.of(new Bank(1L, "Bank1", new Date(), "1236547890", "bank1@gmail.com", true, null));

    @Test
    void saveBank() throws Exception {
        when(bankService.saveBank(any(Bank.class))).thenReturn(bank.get());

        mockMvc.perform(post("/api/bank/add/bank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bank)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void saveBankIfErrorInValidation() {
        List<ObjectError> errors = new ArrayList<>();
        ObjectError error = new ObjectError("bank", "Bank name must not be null");
        errors.add(error);
        when(result.hasErrors()).thenReturn(true);
        when(result.getAllErrors()).thenReturn(errors);

        when(bankController.saveBank(bank.get(),result))
                .thenThrow(new ConstraintViolationException("Validation failed!",null,"Not Found!"));

        assertThrows(ConstraintViolationException.class,
                ()-> bankController.saveBank(bank.get(),result));
    }

    @Test
    void getAllBanks() throws Exception {
        List<Bank> listBank = Arrays.asList(
                new Bank(1L, "Bank1", new Date(), "1236547890", "bank1@gmail.com", true, null),
                new Bank(2L, "Bank2", new Date(), "9856741230", "bank2@gmail.com", true, null)
        );

        when(bankService.findAllBanks()).thenReturn(listBank);
        mockMvc.perform(get("/api/bank/get/bank"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.size()").value(listBank.size()))
                .andDo(print());
    }

//    @Test
    void getAllBanksIfErrorInValidation() {
        List<ObjectError> errors = new ArrayList<>();
        ObjectError error = new ObjectError("bank", "No banks found!");
        errors.add(error);
        when(result.hasErrors()).thenReturn(true);
        when(result.getAllErrors()).thenReturn(errors);

        when(bankController.getAllBanks())
                .thenThrow(new ConstraintViolationException("Validation failed!",null,"Not Found!"));

        assertThrows(ConstraintViolationException.class,
                ()-> bankController.getAllBanks());
    }

    @Test
    void getSingleBank() throws Exception {
        when(bankService.findBankById(id)).thenReturn(bank.get());

        mockMvc.perform(get("/api/bank/get/bank/id/{bankId}", id))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.bankId").value(id))
                .andExpect(jsonPath("$.bankName").value(bank.get().getBankName()))
                .andExpect(jsonPath("$.bankEmail").value(bank.get().getBankEmail()))
                .andDo(result -> {
                    System.out.println(bank);
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void getSingleBankByName() throws Exception {
        when(bankService.findBankByName(name)).thenReturn(bank.get());

        mockMvc.perform(get("/api/bank/get/bank/{bankName}", name))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.bankId").value(id))
                .andExpect(jsonPath("$.bankName").value(bank.get().getBankName()))
                .andExpect(jsonPath("$.bankEmail").value(bank.get().getBankEmail()))
                .andDo(result -> {
                    System.out.println(bank);
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void updateBank() throws Exception {
        Bank oldBank = bank.get();
        Bank updatedBank = new Bank(1L, "Bank1", new Date(), "1236547890", "bank1@gmail.com", true, null);

        when(bankService.findBankById(id)).thenReturn(bank.get());
        when(bankService.updateBank(id, updatedBank)).thenReturn(oldBank);

        mockMvc.perform(put("/api/bank/update/bank/{branchId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBank)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateBankIfErrorInValidation() {
        List<ObjectError> errors = new ArrayList<>();
        ObjectError error = new ObjectError("bank", "Error occurred while updating!");
        errors.add(error);
        when(result.hasErrors()).thenReturn(true);
        when(result.getAllErrors()).thenReturn(errors);

        when(bankController.updateBank(id,bank.get(),result))
                .thenThrow(new ConstraintViolationException("Validation failed!",null,"Not Found!"));

        assertThrows(ConstraintViolationException.class,
                ()-> bankController.updateBank(id,bank.get(),result));
    }

    @Test
    void deleteBank() throws Exception {
        doNothing().when(bankService).deleteBank(id);
        mockMvc.perform(delete("/api/bank/delete/bank/{bankId}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
