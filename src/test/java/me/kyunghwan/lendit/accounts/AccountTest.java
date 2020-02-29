package me.kyunghwan.lendit.accounts;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    @Test
    public void Account_생성_테스트() {
        String email = "email";
        String password = "password";
        long deposit = 50000;

        Account account = Account.builder()
                .email(email)
                .password(password)
                .role(AccountRole.USER)
                .deposit(deposit)
                .build();

        assertThat(account).isNotNull();
        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(account.getPassword()).isEqualTo(password);
        assertThat(account.getRole()).isEqualTo(AccountRole.USER);
        assertThat(account.getDeposit()).isEqualTo(deposit);
    }

}