package me.kyunghwan.lendit.orders;


import me.kyunghwan.lendit.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.context.annotation.Description;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class OrderControllerTest extends BaseControllerTest {

    @Test
    public void DTO_테스트() throws Exception {
        OrderRequestDto requestDto = OrderRequestDto.builder()
                .name("상품1")
                .quantity(2)
                .build();

        mockMvc.perform(post("/api/orders")
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithUser())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
        ;
    }

    @Description("USER의 인증 토큰을 발급하는 메서드")
    private String getAuthTokenWithUser() throws Exception {
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", appProperties.getUserUsername())
                .param("password", appProperties.getUserPassword())
                .param("grant_type", "password"));

        String contentAsString = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser jackson2JsonParser = new Jackson2JsonParser();
        String token = jackson2JsonParser.parseMap(contentAsString).get("access_token").toString();
        return "Bearer " + token;
    }

}