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

    private boolean isUser(Account account) {
        return account.getRole() == AccountRole.USER;
    }

    private ResponseEntity badRequest() {
        return ResponseEntity.badRequest().build();
    }

}
