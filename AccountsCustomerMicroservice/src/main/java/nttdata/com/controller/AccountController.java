package nttdata.com.controller;

import lombok.AllArgsConstructor;
import nttdata.com.dto.AccountDTO;
import nttdata.com.dto.TransactionDTO;
import nttdata.com.service.impl.AccountServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;

    @PostMapping(value = "/createAccount", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        return accountServiceImpl.createAccount(accountDTO);
    }

    @GetMapping(value= "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<AccountDTO> getAccountById(@PathVariable String id) {
        return accountServiceImpl.getAccountById(id);
    }

    @PostMapping(value = "/{accountId}/transactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<AccountDTO> addTransaction(@PathVariable String accountId,
                                           @RequestBody TransactionDTO transactionDTO) {
        return accountServiceImpl.addTransaction(accountId, transactionDTO);
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<AccountDTO> updateAccount(@PathVariable String id, @RequestBody AccountDTO accountDTO) {
        return accountServiceImpl.updateAccount(id, accountDTO);
    }
    @GetMapping(value = "/{accountId}/transactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TransactionDTO> getTransactionsByAccountId(@PathVariable String accountId) {
        return accountServiceImpl.getTransactionsByAccountId(accountId);
    }
}