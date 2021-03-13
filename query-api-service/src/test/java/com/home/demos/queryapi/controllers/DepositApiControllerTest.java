package com.home.demos.queryapi.controllers;

import com.home.demos.queryapi.dto.DepositCommandResult;
import com.home.demos.queryapi.services.DepositApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DepositApiController.class)
class DepositApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepositApiService depositApiService;
    private final DepositCommandResult expectedResult = new DepositCommandResult("abc123", 0);

    @Test
    void takeDepositCommandResultWhenEverythingGoesRightThenShouldBeExpectedResult() throws Exception {
        Mockito.when(depositApiService.takeCreatedDepositResult(expectedResult.getRequestID()))
                .thenReturn(expectedResult);

        mockMvc.perform(get("/deposit-request-api/take-command-result/".concat(expectedResult.getRequestID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestID").value(expectedResult.getRequestID()))
                .andExpect(jsonPath("$.resultCode").value(expectedResult.getResultCode()));
    }
}