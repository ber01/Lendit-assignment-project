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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity addOrder(@RequestBody OrderRequestDto requestDto,
                                   @AuthenticationPrincipal AccountAdapter accountAdapter) {
        String str = requestDto.getName() + " " + requestDto.getQuantity();
        System.out.println(str);
        Account account = accountAdapter.getAccount();
        return null;
    }

}
