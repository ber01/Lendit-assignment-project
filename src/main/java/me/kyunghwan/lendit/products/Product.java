package me.kyunghwan.lendit.products;

import lombok.*;

import javax.persistence.*;

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

    @Builder
    public Product(String name, Long price, Long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

}
