package me.kyunghwan.lendit.orders;

import lombok.*;
import me.kyunghwan.lendit.accounts.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "ORDERS")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long totalPrice;

    @Column
    private LocalDateTime orderedTime;

    @ManyToOne
    private Account account;

    @Builder
    public Order(Long totalPrice, LocalDateTime orderedTime, Account account) {
        this.totalPrice = totalPrice;
        this.orderedTime = orderedTime;
        this.account = account;
    }

}
