package me.kyunghwan.lendit.orders;

import me.kyunghwan.lendit.accounts.Account;
import me.kyunghwan.lendit.accounts.AccountRole;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    public void Order_생성_테스트() {
        Account account = Account.builder()
                .email("email@email.com")
                .password("password")
                .deposit(5000L)
                .role(AccountRole.USER)
                .build();

        long totalPrice = 10000L;
        Order order = Order.builder()
                .totalPrice(totalPrice)
                .orderedTime(LocalDateTime.now())
                .account(account)
                .build();

        account.addOrder(order);

        Order accountOrder = account.getOrdersList().get(0);
        assertThat(accountOrder.getTotalPrice()).isEqualTo(totalPrice);
    }

}