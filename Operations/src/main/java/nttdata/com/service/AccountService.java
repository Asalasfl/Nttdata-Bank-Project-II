package nttdata.com.service;

import nttdata.com.dto.AccountDTO;
import nttdata.com.dto.TransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<AccountDTO> getAccountById(String id);
    Mono<AccountDTO> createAccount(AccountDTO accountDTO);
    Mono<AccountDTO> updateAccount(String id, AccountDTO accountDTO);
    Mono<AccountDTO> addTransaction(String accountId, TransactionDTO transactionDTO);

    Flux<TransactionDTO> getTransactionsByAccountId(String accountId);
}
