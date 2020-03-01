package me.kyunghwan.lendit.products;

import me.kyunghwan.lendit.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends BaseControllerTest {

    @Autowired
    ProductRepository productRepository;

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

    @Test
    @Description("상품을 조회할 때 ADMIN인 경우 상품을 등록하는 링크가 존재하는 테스트")
    public void Product_조회_ADMIN() throws Exception {
        this.mockMvc.perform(get("/api/products")
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithAdmin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[4].rel").value("register-product"))
        ;
    }

    @Test
    @Description("하나의 상품을 조회하면 해당 상품의 정보와 전체 조회 링크가 존재하는 테스트")
    public void Product_하나_조회() throws Exception {
        long productId = new Random().nextInt(30) + 1;
        this.mockMvc.perform(get("/api/products/" + productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[1].rel").value("lookup-product"))
        ;
    }

    @Test
    @Description("상품을 등록한 사람이 하나의 상품을 조회하면 업데이트 링크가 존재하는 테스트")
    public void Product_하나_조회_ADMIN() throws Exception {
        long productId = new Random().nextInt(30) + 1;
        this.mockMvc.perform(get("/api/products/" + productId)
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithAdmin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[1].rel").value("lookup-product"))
                .andExpect(jsonPath("$.links[2].rel").value("update-product"))
        ;
    }

    @Test
    @Description("상품을 등록한 사람이 상품을 성공적으로 삭제하는 테스트")
    public void Product_삭제_테스트_ADMIN() throws Exception {
        long productId = 1L;

        this.mockMvc.perform(delete("/api/products/" + productId)
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithAdmin()))
                    .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[1].rel").value("lookup-product"))
                .andExpect(jsonPath("$.links[2].rel").value("register-product"))
        ;

        Optional<Product> optional = productRepository.findById(productId);
        assertThat(optional).isEmpty();
    }

    @Test
    @Description("USER 권한이 삭제 요청시 실패하는 테스트")
    public void Product_삭제_테스트_USER() throws Exception {
        long productId = 1L;

        this.mockMvc.perform(delete("/api/products/" + productId)
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithUser()))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        Optional<Product> optional = productRepository.findById(productId);
        assertThat(optional).isNotNull();
    }

    @Test
    @Description("ADMIN 권한이지만 등록하지 않는 유저가 삭제 요청시 실패하는 테스트")
    public void Product_삭제_테스트_ANOTHER_ADMIN() throws Exception {
        long productId = 1L;
        this.mockMvc.perform(delete("/api/products/" + productId)
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithAnotherAdmin()))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        Optional<Product> optional = productRepository.findById(productId);
        assertThat(optional).isNotNull();
    }

    @Test
    @Description("상품을 등록한 사람이 상품 수정에 성공하는 테스트")
    public void Product_수정_테스트() throws Exception {
        long productId = 2L;
        Product product = productRepository.findById(productId).get();

        ProductDto productDto = ProductDto.builder()
                .name("수정 이름")
                .build();

        this.mockMvc.perform(put("/api/products/" + productId)
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithAdmin())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        Product updateProduct = productRepository.findById(productId).get();

        assertThat(product.getName()).isNotEqualTo(updateProduct.getName());
    }

    @Test
    @Description("ADMIN 권한의 다른 유저가 상품 수정에 실패하는 테스트")
    public void Product_수정_테스트_ANOTHER_ADMIN() throws Exception {
        long productId = 2L;

        ProductDto productDto = ProductDto.builder()
                .name("수정 이름")
                .build();

        this.mockMvc.perform(put("/api/products/" + productId)
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithAnotherAdmin())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @Description("USER 권한의 유저가 상품 수정에 실패하는 테스트")
    public void Product_수정_테스트_USER() throws Exception {
        long productId = 1L;

        ProductDto productDto = ProductDto.builder()
                .name("수정 이름")
                .build();

        this.mockMvc.perform(put("/api/products/" + productId)
                    .header(HttpHeaders.AUTHORIZATION, getAuthTokenWithUser())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Description("ANOTHER ADMIN의 인증 토큰을 발급하는 메서드")
    private String getAuthTokenWithAnotherAdmin() throws Exception {
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", "anotherAdmin@email.com")
                .param("password", "password")
                .param("grant_type", "password"));

        String contentAsString = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser jackson2JsonParser = new Jackson2JsonParser();
        String token = jackson2JsonParser.parseMap(contentAsString).get("access_token").toString();
        return "Bearer " + token;
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