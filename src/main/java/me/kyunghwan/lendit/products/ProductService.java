package me.kyunghwan.lendit.products;

import lombok.RequiredArgsConstructor;
import me.kyunghwan.lendit.accounts.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product productRegistration(ProductDto productDto, Account account) {
        return productRepository.save(productDto.toEntity(account));
    }

}
