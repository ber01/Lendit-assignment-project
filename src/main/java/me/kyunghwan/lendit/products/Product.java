package me.kyunghwan.lendit.products;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.kyunghwan.lendit.accounts.Account;
import me.kyunghwan.lendit.accounts.AccountSerializer;

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
    @JsonSerialize(using = AccountSerializer.class)
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
