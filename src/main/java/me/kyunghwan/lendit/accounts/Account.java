package me.kyunghwan.lendit.accounts;

import lombok.*;
import me.kyunghwan.lendit.products.Product;

import javax.persistence.*;
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

    /*
    @OneToMany
    private List<Product> cartsList;

    @OneToMany
    private List<Product> ordersList;
    */

}
