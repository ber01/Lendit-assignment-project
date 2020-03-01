package me.kyunghwan.lendit.products;

import lombok.*;
import me.kyunghwan.lendit.accounts.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @Column
    private Long quantity;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    private Account account;

    @Builder
    public Product(String name, Long price, Long quantity, LocalDateTime createdAt, Account account) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.account = account;
    }

}
