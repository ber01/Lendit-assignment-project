package me.kyunghwan.lendit.index;

import me.kyunghwan.lendit.accounts.Account;
import me.kyunghwan.lendit.accounts.AccountAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal AccountAdapter accountAdapter) {
        if (accountAdapter == null) {
            return "로그인 하지 않은 사용자";
        }
        Account account = accountAdapter.getAccount();
        return account.getEmail() + " " + account.getRole();
    }

}
