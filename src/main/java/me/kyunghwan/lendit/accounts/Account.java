package me.kyunghwan.lendit.accounts;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
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

    /*
    @OneToMany
    private List<Product> cartsList;

    @OneToMany
    private List<Product> ordersList;
    */

    @Builder
    public Account(String email, String password, AccountRole role, Long deposit) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.deposit = deposit;
    }

}
