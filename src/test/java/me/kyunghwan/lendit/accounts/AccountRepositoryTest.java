package me.kyunghwan.lendit.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void Account_등록_조회_테스트() {
        String email = "email";
        String password = "password";
        long deposit = 50000;

        Account account = accountRepository.save(Account.builder()
                .email(email)
                .password(password)
                .role(AccountRole.USER)
                .deposit(deposit)
                .build());

        assertThat(account).isNotNull();
        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(account.getPassword()).isEqualTo(password);
        assertThat(account.getRole()).isEqualTo(AccountRole.USER);
        assertThat(account.getDeposit()).isEqualTo(deposit);
    }

}