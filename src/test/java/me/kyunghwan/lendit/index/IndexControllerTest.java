package me.kyunghwan.lendit.index;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Description("인증 토큰을 발급하는 메서드")
    private String getAuthToken() throws Exception {
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic("len", "dit"))
                .param("username", "lendit@email.com")
                .param("password", "password")
                .param("grant_type", "password"));

        String contentAsString = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser jackson2JsonParser = new Jackson2JsonParser();
        String token = jackson2JsonParser.parseMap(contentAsString).get("access_token").toString();
        return "Bearer " + token;
    }

    @Test
    public void 현재_사용자_조회_테스트() throws Exception {
        this.mockMvc.perform(get("/")
                .header(HttpHeaders.AUTHORIZATION, getAuthToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 현재_없는_사용자_조회_테스트() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }


}