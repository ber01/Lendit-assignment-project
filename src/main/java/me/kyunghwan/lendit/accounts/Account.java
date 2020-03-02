package me.kyunghwan.lendit.accounts;

import lombok.*;
import me.kyunghwan.lendit.orders.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Column
    private Long deposit;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    private List<Order> ordersList = new ArrayList<>();

    public void addOrder(Order order) {
        order.setAccount(this);
        this.getOrdersList().add(order);
    }

    @Builder
    public Account(String email, String password, AccountRole role, Long deposit) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.deposit = deposit;
    }

}
