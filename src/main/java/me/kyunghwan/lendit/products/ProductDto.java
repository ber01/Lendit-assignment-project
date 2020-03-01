package me.kyunghwan.lendit.products;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kyunghwan.lendit.accounts.Account;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @NoArgsConstructor
public class ProductDto {

    @NotEmpty
    private String name;
    @Min(0)
    private Long price;
    @Min(0)
    private Long quantity;

    @Builder
    public ProductDto(String name, Long price, Long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product toEntity(Account account) {
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .quantity(this.quantity)
                .createdAt(LocalDateTime.now())
                .account(account)
                .build();
    }

}
