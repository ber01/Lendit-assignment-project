package me.kyunghwan.lendit.accounts;

import lombok.*;
import me.kyunghwan.lendit.products.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private AccountRole role;

    private Long deposit;

    private List<Product> cartsList;

    private List<Product> ordersList;

}
