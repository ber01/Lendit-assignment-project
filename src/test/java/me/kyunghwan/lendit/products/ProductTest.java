package me.kyunghwan.lendit.products;


import me.kyunghwan.lendit.accounts.AccountRole;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    public void Product_생성_테스트() {
        String productName = "productName";
        long price = 10000L;
        long quantity = 30L;

        Product product = Product.builder()
                .name(productName)
                .price(price)
                .quantity(quantity)
                .build();

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getQuantity()).isEqualTo(quantity);
    }

}