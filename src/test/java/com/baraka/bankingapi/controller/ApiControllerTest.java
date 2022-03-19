package com.baraka.bankingapi.controller;

import com.baraka.bankingapi.model.InternationalMoneyTransferDto;
import com.baraka.bankingapi.model.MoneyTransferDto;
import com.baraka.bankingapi.model.NewAccountDto;
import com.baraka.bankingapi.view.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiControllerTest {

    public static final String DEFAULT_ACCOUNT_ID = "a1";
    public static final String SECOND_ACCOUNT_ID = "1";
    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    void createAccount() throws Exception {
        NewAccountDto accountDto = new NewAccountDto();
        accountDto.setAccountId(DEFAULT_ACCOUNT_ID);
        accountDto.setOwner("a1");
        String record = new ObjectMapper().writeValueAsString(accountDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .content(record)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }


    @Test
    @Order(2)
    void getAccounts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner").isNotEmpty());
    }

    @Test
    @Order(3)
    void getBalanceByAccountId() throws Exception {
        assertTrue(mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + DEFAULT_ACCOUNT_ID + "/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString().contains("0"));
    }

    @Test
    @Order(4)
    void deposit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/" + DEFAULT_ACCOUNT_ID + "/deposit/10.5436")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(10.5436));
    }

    @Test
    @Order(5)
    void withdrawal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/" + DEFAULT_ACCOUNT_ID + "/withdrawal/5.5436")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(5));
    }

    @Test
    @Order(6)
    void transfer() throws Exception {
        createAccountForTest();
        MoneyTransferDto dto = MoneyTransferDto.builder()
                .amount(BigDecimal.ONE)
                .fromAccountId(DEFAULT_ACCOUNT_ID)
                .toAccountId(SECOND_ACCOUNT_ID)
                .build();
        String record = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(record))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + DEFAULT_ACCOUNT_ID + "/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(0, BigDecimal.valueOf(4).compareTo(new BigDecimal(result)));
        String toAccountResult = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + SECOND_ACCOUNT_ID + "/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(0, BigDecimal.valueOf(1).compareTo(new BigDecimal(toAccountResult)));

    }

    @Test
    @Order(7)
    void internationalTransfer() throws Exception {
        MoneyTransferDto dto = InternationalMoneyTransferDto.internationalBuilder()
                .amount(BigDecimal.ONE)
                .currency(Currency.EUR)
                .fromAccountId(DEFAULT_ACCOUNT_ID)
                .toAccountId(SECOND_ACCOUNT_ID)
                .build();
        String record = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/internationalTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(record))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + DEFAULT_ACCOUNT_ID + "/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(0, BigDecimal.valueOf(2.9).compareTo(new BigDecimal(result)));
        String toAccountResult = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + SECOND_ACCOUNT_ID + "/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(0, BigDecimal.valueOf(2.1).compareTo(new BigDecimal(toAccountResult)));
    }


    @Test
    @Order(7)
    void withdrawal_notEnoughMoney() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/" + DEFAULT_ACCOUNT_ID + "/withdrawal/500")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @Order(8)
    void withdrawal_NullAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/" + DEFAULT_ACCOUNT_ID + "/withdrawal/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(9)
    void createAccount_AlreadyExists() throws Exception {
        NewAccountDto accountDto = new NewAccountDto();
        accountDto.setAccountId(DEFAULT_ACCOUNT_ID);
        accountDto.setOwner("a1");
        String record = new ObjectMapper().writeValueAsString(accountDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .content(record)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(15)
    void delete() throws Exception {
        createAccountForTest();
        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/" + DEFAULT_ACCOUNT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    private void createAccountForTest() throws Exception {
        NewAccountDto accountDto = new NewAccountDto();
        accountDto.setAccountId(SECOND_ACCOUNT_ID);
        accountDto.setOwner("a1");
        String record = new ObjectMapper().writeValueAsString(accountDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                .content(record)
                .contentType(MediaType.APPLICATION_JSON));
    }
}