package com.socialmedia.rest.open;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
public class AuthenticationControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void correctRegistrationUser_thenStatus_isCreated() throws Exception{
        HashMap<String,Object> userDto = new HashMap<>();
        userDto.put("password","alexAlex002");
        userDto.put("email","alexAlex@gmail.com");
        userDto.put("name","Alex");

        mockMvc.perform(
                        post("/socialmedia/auth/signup")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value("User:" + userDto.get("email") + " has been created."));

    }

    @Test
    @Transactional
    public void givenLogin_whenInvalidEmailOrPassword_thenStatus_isBadRequest() throws Exception{
        HashMap<String,String> userDto = new HashMap<>();
        userDto.put("password","alexAlex002");
        userDto.put("email","alex");
        userDto.put("name","Alex");
        mockMvc.perform(
                        post("/socialmedia/auth/signup")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").value("Invalid email: Please,try again"));
    }

    @Test
    @Sql(value = {"/create-correct-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Transactional
    public void correctLoginUser_thenStatus_isOk() throws Exception{
        HashMap<String,String> userDto = new HashMap<>();
        userDto.put("password","alexAlex002");
        userDto.put("email","alexAlex@gmail.com");
        mockMvc.perform(
                        post("/socialmedia/auth/signin")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").isNotEmpty());

    }


}
