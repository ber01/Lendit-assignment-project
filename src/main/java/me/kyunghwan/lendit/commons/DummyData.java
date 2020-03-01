package me.kyunghwan.lendit.commons;

import lombok.RequiredArgsConstructor;
import me.kyunghwan.lendit.accounts.Account;
import me.kyunghwan.lendit.accounts.AccountRepository;
import me.kyunghwan.lendit.accounts.AccountRole;
import me.kyunghwan.lendit.accounts.AccountService;
import me.kyunghwan.lendit.products.Product;
import me.kyunghwan.lendit.products.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class DummyData implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        accountRepository.deleteAll();

        Account admin = Account.builder()
                .email("lendit@email.com")
                .password("password")
                .deposit(999999999L)
                .role(AccountRole.ADMIN)
                .build();

        accountService.saveAccount(admin);

        Account user = Account.builder()
                .email("user@email.com")
                .password("password")
                .deposit(50000L)
                .role(AccountRole.USER)
                .build();

        accountService.saveAccount(user);

        IntStream.rangeClosed(1, 30).forEach(i -> {
            productRepository.save(Product.builder()
                    .name("상품" + i)
                    .price(500L * i)
                    .quantity((long) i)
                    .build());
        });
    }

}
