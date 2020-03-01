package me.kyunghwan.lendit.configs;


import me.kyunghwan.lendit.accounts.Account;
import me.kyunghwan.lendit.accounts.AccountRole;
import me.kyunghwan.lendit.accounts.AccountService;
import me.kyunghwan.lendit.common.BaseControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @Description("인증 토큰을 발급받는 테스트")
    public void getAuthToken() throws Exception {
        // given
        String username = "minkh@email.com";
        String password = "password";

        Account account = Account.builder()
                .email(username)
                .password(password)
                .role(AccountRole.USER)
                .build();

        this.accountService.saveAccount(account);

        // when & then
        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}