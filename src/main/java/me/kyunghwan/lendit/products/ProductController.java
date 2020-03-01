package me.kyunghwan.lendit.products;

import lombok.RequiredArgsConstructor;
import me.kyunghwan.lendit.accounts.Account;
import me.kyunghwan.lendit.accounts.AccountAdapter;
import me.kyunghwan.lendit.accounts.AccountRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RequestMapping(value = "/api/products", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity productRegistration(@RequestBody @Valid ProductDto productDto,
                                              Errors errors,
                                              @AuthenticationPrincipal AccountAdapter accountAdapter) {
        if (errors.hasErrors()) {
            return badRequest();
        }

        Account account = accountAdapter.getAccount();
        if (isUser(account)) {
            return badRequest();
        }

        Product createProduct = productService.productRegistration(productDto, account);

        ControllerLinkBuilder linkBuilder = linkTo(ProductController.class).slash(createProduct.getId());
        URI uri = linkBuilder.toUri();

        ProductResource productResource = new ProductResource(createProduct);
        productResource.add(linkTo(ProductController.class).withRel("lookup-product"));
        productResource.add(new Link("테스트").withRel("profile"));

        return ResponseEntity.created(uri).body(productResource);
    }

    @GetMapping
    public ResponseEntity productLookup(Pageable pageable,
                                        PagedResourcesAssembler<Product> assembler,
                                        @AuthenticationPrincipal AccountAdapter accountAdapter) {
        Page<Product> products = productService.productLookup(pageable);
        PagedResources<Resource<Product>> pagedResources = assembler.toResource(products, e -> new ProductResource(e));

        if (accountAdapter != null && accountAdapter.getAccount().getRole() == AccountRole.ADMIN) {
            pagedResources.add(linkTo(ProductController.class).withRel("register-product"));
        }

        pagedResources.add(new Link("테스트").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{productId}")
    public ResponseEntity oneProductLookup(@PathVariable Long productId,
                                           @AuthenticationPrincipal AccountAdapter accountAdapter) {
        Product product = productService.oneProductLookup(productId);
        ProductResource productResource = new ProductResource(product);
        productResource.add(linkTo(ProductController.class).withRel("lookup-product"));
        if (accountAdapter != null) {
            Account account = accountAdapter.getAccount();
            if (isEqualsAccount(product, account)) {
                productResource.add(linkTo(ProductController.class).withRel("update-product"));
            }
        }

        productResource.add(new Link("테스트").withRel("profile"));

        return ResponseEntity.ok(productResource);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity productDelete(@PathVariable Long productId,
                                        @AuthenticationPrincipal AccountAdapter accountAdapter) {
        Account account = accountAdapter.getAccount();
        if (isUser(account)) {
            System.out.println("어드민 아님");
            return badRequest();
        }

        Product deleteProduct = productService.oneProductLookup(productId);
        if (account.getRole() == AccountRole.ADMIN && !account.equals(deleteProduct.getAccount())) {
            System.out.println("어드민이긴 한데 다른 사용자");
            return badRequest();
        }

        productService.productDelete(deleteProduct);
        System.out.println("상품을 등록한 어드민");

        ProductResource productResource = new ProductResource(deleteProduct);
        productResource.add(linkTo(ProductController.class).withRel("lookup-product"));
        productResource.add(linkTo(ProductController.class).withRel("register-product"));
        productResource.add(new Link("테스트").withRel("profile"));

        return ResponseEntity.ok(productResource);
    }

    @PutMapping("{productId}")
    public ResponseEntity productUpdate(@PathVariable Long productId,
                                        @RequestBody ProductDto productDto,
                                        @AuthenticationPrincipal AccountAdapter accountAdapter) {
        Account account = accountAdapter.getAccount();
        if (isUser(account)) {
            System.out.println("유저임");
            return badRequest();
        }

        Product updateProduct = productService.oneProductLookup(productId);
        if (account.getRole() == AccountRole.ADMIN && !account.equals(updateProduct.getAccount())) {
            System.out.println("어드민이긴 한데 다른 사용자임");
            return badRequest();
        }

        Product product = productService.productUpdate(updateProduct, productDto);
        ProductResource productResource = new ProductResource(product);
        productResource.add(linkTo(ProductController.class).withRel("lookup-product"));
        productResource.add(new Link("테스트").withRel("profile"));

        return ResponseEntity.ok(productResource);
    }

    private boolean isEqualsAccount(Product product, Account account) {
        return product.getAccount().equals(account);
    }

    private boolean isUser(Account account) {
        return account.getRole() == AccountRole.USER;
    }

    private ResponseEntity badRequest() {
        return ResponseEntity.badRequest().build();
    }

}
