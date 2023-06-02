package com.socialmedia.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
public class UserMessageControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(value = {"/create-correct-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-correct-user2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-correct-friends.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Transactional
    public void correctSendMessage_thenStatus_isOk() throws Exception {
        mockMvc.perform(
                        post("/socialmedia/message/{friendEmail}","olyaOlya@gmail.com")
                                .content(objectMapper.writeValueAsString(createMessageDto()))
                                .header("Authorization","Bearer " + getUserTokenAlex())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value("Message has been send."));
    }

    @Test
    @Sql(value = {"/create-correct-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-correct-user2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-correct-message.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Transactional
    public void correctDeletionMessage_thenStatus_isOk() throws Exception {
        mockMvc.perform(
                        delete("/socialmedia/message//{message_id}","22468a7e-b4f9-4e00-ad75-ba7adf0adeb7")
                                .header("Authorization","Bearer " + getUserTokenOlya())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value("Message has been deleted"));
    }



    private HashMap<String, Object> createMessageDto(){
        HashMap<String, Object> messageDto = new HashMap<>();
        messageDto.put("textMessage","Hello");
        messageDto.put("friendRequest_id","2a12b821-4162-4654-9cf0-211099850eec");
        return messageDto;
    }

    private String getUserTokenOlya() throws Exception{
        HashMap<String,String> userDto = new HashMap<>();
        userDto.put("password","olyaOlya003");
        userDto.put("email","olyaOlya@gmail.com");
        ResultActions resultActions = mockMvc.perform(
                        post("/socialmedia/auth/signin")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.data.token").isNotEmpty());
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String dataAuth = jsonParser.parseMap(resultActions.andReturn().getResponse().getContentAsString()).get("data").toString();
        String[] data = dataAuth.split(",");
        String resultToken = data[1];
        return resultToken.substring(7,resultToken.length() - 1);
    }

    private String getUserTokenAlex() throws Exception{
        HashMap<String,String> userDto = new HashMap<>();
        userDto.put("password","alexAlex002");
        userDto.put("email","alexAlex@gmail.com");
        ResultActions resultActions = mockMvc.perform(
                        post("/socialmedia/auth/signin")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.data.token").isNotEmpty());
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String dataAuth = jsonParser.parseMap(resultActions.andReturn().getResponse().getContentAsString()).get("data").toString();
        String[] data = dataAuth.split(",");
        String resultToken = data[1];
        return resultToken.substring(7,resultToken.length() - 1);
    }
}
