package me.kyunghwan.lendit.products;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void Product_등록_조회_테스트() {
        String productName = "productName";
        long price = 10000L;
        long quantity = 30L;

        Product product = productRepository.save(Product.builder()
                .name(productName)
                .price(price)
                .quantity(quantity)
                .build());

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getQuantity()).isEqualTo(quantity);
    }

}