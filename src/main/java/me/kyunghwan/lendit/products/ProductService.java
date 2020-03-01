package me.kyunghwan.lendit.products;

import lombok.RequiredArgsConstructor;
import me.kyunghwan.lendit.accounts.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product productRegistration(ProductDto productDto, Account account) {
        return productRepository.save(productDto.toEntity(account));
    }

    @Transactional
    public Page<Product> productLookup(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product oneProductLookup(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException(productId + "에 해당하는 상품 없음"));
    }

    public void productDelete(Product deleteProduct) {
        productRepository.delete(deleteProduct);
    }

}
