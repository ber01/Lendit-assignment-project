package me.kyunghwan.lendit.products;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class ProductResource extends Resource<Product> {

    public ProductResource(Product product, Link... links) {
        super(product, links);
        add(linkTo(ProductController.class).slash(product.getId()).withSelfRel());
    }

}
