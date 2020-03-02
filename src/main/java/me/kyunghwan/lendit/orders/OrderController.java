package me.kyunghwan.lendit.orders;

import lombok.RequiredArgsConstructor;
import me.kyunghwan.lendit.accounts.Account;
import me.kyunghwan.lendit.accounts.AccountAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity addOrder(@RequestBody List<OrderRequestDto> dtoList,
                                   @AuthenticationPrincipal AccountAdapter accountAdapter) {
        for (OrderRequestDto dto : dtoList) {
            System.out.println(dto.getName() + " " + dto.getQuantity());
        }
        Account account = accountAdapter.getAccount();
        return null;
    }

}
