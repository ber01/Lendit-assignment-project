package me.kyunghwan.lendit.commons;

import lombok.RequiredArgsConstructor;
import me.kyunghwan.lendit.accounts.Account;
import me.kyunghwan.lendit.accounts.AccountRepository;
import me.kyunghwan.lendit.accounts.AccountRole;
import me.kyunghwan.lendit.accounts.AccountService;
import me.kyunghwan.lendit.products.ProductDto;
import me.kyunghwan.lendit.products.ProductRepository;
import me.kyunghwan.lendit.products.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class DummyData implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ProductRepository productRepository;
    private final AppProperties appProperties;
    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        accountRepository.deleteAll();
        productRepository.deleteAll();

        Account admin = Account.builder()
                .email(appProperties.getAdminUsername())
                .password(appProperties.getAdminPassword())
                .deposit(999999999L)
                .role(AccountRole.ADMIN)
                .build();

        accountService.saveAccount(admin);

        Account user = Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .deposit(50000L)
                .role(AccountRole.USER)
                .build();

        accountService.saveAccount(user);

        IntStream.rangeClosed(1, 30).forEach(i -> {
            ProductDto productDto = ProductDto.builder()
                    .name("상품" + i)
                    .price((long) i * 500)
                    .quantity((long) i)
                    .build();

            productService.productRegistration(productDto, admin);
        });
    }

}
