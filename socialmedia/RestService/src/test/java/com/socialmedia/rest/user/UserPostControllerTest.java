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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
public class UserPostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Test
    @Sql(value = {"/create-correct-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Transactional
    public void correctAddPost_thenStatus_isOk() throws Exception {
        mockMvc.perform(
                        post("/socialmedia/post")
                                .content(objectMapper.writeValueAsString(createPostDto()))
                                .header("Authorization","Bearer " + getUserToken())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value("Post has been added."));
    }

    @Test
    @Sql(value = {"/create-correct-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-correct-post.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Transactional
    public void correctUpdatePost_thenStatus_isOk() throws Exception{
        HashMap<String, Object> updatePostDto = createUpdatePostDto();
        mockMvc.perform(
                        put("/socialmedia/post/{post_id}","a8a62b2b-1eb7-48b2-973f-f341c1ab5515")
                                .content(objectMapper.writeValueAsString(updatePostDto))
                                .header("Authorization","Bearer " + getUserToken())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(updatePostDto.get("title")))
                .andExpect(jsonPath("$.data.postText").value(updatePostDto.get("postText")));
    }

    @Test
    @Sql(value = {"/create-correct-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-correct-post.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Transactional
    public void correctDeletionPost_thenStatus_isOk() throws Exception {
        mockMvc.perform(
                        delete("/socialmedia/post/{post_id}","a8a62b2b-1eb7-48b2-973f-f341c1ab5515")
                                .header("Authorization","Bearer " + getUserToken())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value("Post with id:a8a62b2b-1eb7-48b2-973f-f341c1ab5515 was removed."));
    }




    private HashMap<String,Object> createPostDto(){
        HashMap<String,Object> postDto = new HashMap<>();
        postDto.put("title","Germany");
        postDto.put("postText","I'm from Germany");
        postDto.put("image","D:/Downloads/image2.png");
        return postDto;
    }
    private HashMap<String,Object> createUpdatePostDto(){
        HashMap<String,Object> updatePostDto = new HashMap<>();
        updatePostDto.put("title","Belarus");
        updatePostDto.put("postText","I'm from Belarus");
        updatePostDto.put("image","");
        return updatePostDto;
    }

    private String getUserToken() throws Exception{
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
