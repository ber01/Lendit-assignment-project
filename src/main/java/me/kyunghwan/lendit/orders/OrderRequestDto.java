package me.kyunghwan.lendit.orders;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter @NoArgsConstructor
public class OrderRequestDto {

    @NotEmpty
    private String name;

    @Min(1)
    private Integer quantity;

    @Builder
    public OrderRequestDto(@NotEmpty String name, @Min(1) Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

}
