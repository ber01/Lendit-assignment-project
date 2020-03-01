package me.kyunghwan.lendit.products;

import me.kyunghwan.lendit.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.context.annotation.Description;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends BaseControllerTest {

    @Test
    @Description("새로운 상품 등록에 성공하는 테스트")
    public void Product_등록_성공() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name("새로운 상품")
                .price(10000L)
                .quantity(30L)
                .build();

        mockMvc.perform(post("/api/products")
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithAdmin())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.links[0].rel").value("self"))
                .andExpect(jsonPath("$.links[1].rel").value("lookup-product"))
                .andExpect(jsonPath("$.links[2].rel").value("profile"))
        ;
    }

    @Test
    @Description("새로운 상품 등록에 실패하는 테스트")
    public void Product_등록_실패() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name("새로운 상품")
                .price(10000L)
                .quantity(30L)
                .build();

        mockMvc.perform(post("/api/products")
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithUser())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @Description("상품 전체를 조회하는 테스트")
    public void Product_조회() throws Exception {
        this.mockMvc.perform(get("/api/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
        ;
    }

    @Test
    @Description("상품을 5개씩 조회하는 테스트")
    public void Product_조회_5개() throws Exception {
        this.mockMvc.perform(get("/api/products")
                    .param("page", "2")
                    .param("size", "5")
                    .param("sort", "id,ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
        ;
    }

    @Description("ADMIN의 인증 토큰을 발급하는 메서드")
    private String getAuthTokenWithAdmin() throws Exception {
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", appProperties.getAdminUsername())
                .param("password", appProperties.getAdminPassword())
                .param("grant_type", "password"));

        String contentAsString = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser jackson2JsonParser = new Jackson2JsonParser();
        String token = jackson2JsonParser.parseMap(contentAsString).get("access_token").toString();
        return "Bearer " + token;
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