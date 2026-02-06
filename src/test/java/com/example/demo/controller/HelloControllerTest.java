package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addSuccess() throws Exception {
        mockMvc.perform(get("/add").param("a", "2").param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void missingParam() throws Exception {
        mockMvc.perform(get("/add").param("b", "3"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void outOfRange() throws Exception {
        mockMvc.perform(get("/add").param("a", "-1").param("b", "3"))
                .andExpect(status().isBadRequest());
    }
}
