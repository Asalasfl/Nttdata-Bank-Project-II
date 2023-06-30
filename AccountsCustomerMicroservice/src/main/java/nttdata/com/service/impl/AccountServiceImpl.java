package nttdata.com.service.impl;

import lombok.AllArgsConstructor;
import nttdata.com.dto.AccountDTO;
import nttdata.com.dto.TransactionDTO;
import nttdata.com.model.Account;
import nttdata.com.model.Transaction;
import nttdata.com.repository.AccountRepository;
import nttdata.com.repository.TransactionRepository;
import nttdata.com.service.AccountService;
import nttdata.com.utils.AccountConverter;
import nttdata.com.utils.TransactionConverter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static nttdata.com.utils.AccountConverter.accountToAccountDTO;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
        Account account = AccountConverter.accountDTOToAccount(accountDTO);
        return accountRepository.save(account)
                .map(AccountConverter::accountToAccountDTO);
    }
    @Override
    public Mono<AccountDTO> getAccountById(String id) {
        return accountRepository.findById(id)
                .map(AccountConverter::accountToAccountDTO);
    }

    @Override
    public Mono<AccountDTO> updateAccount(String id, AccountDTO accountDTO) {
        return accountRepository.findById(id)
                .flatMap(account -> {
                    account.setType(accountDTO.getType());
                    account.setBalance(accountDTO.getBalance());
                    // Convertir Flux<TransactionDTO> a Flux<Transaction>
                    Flux<Transaction> transactions = accountDTO.getTransactions()
                            .flatMap(transactionDTO -> Mono.just(TransactionConverter.transactionDTOToTransaction(transactionDTO)));
                    account.setTransactions(transactions);

                    return accountRepository.save(account)
                            .map(AccountConverter::accountToAccountDTO);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }
    @Override
    public Mono<AccountDTO> addTransaction(String accountId, TransactionDTO transactionDTO) {
        return accountRepository.findById(accountId)
                .flatMap(account -> {
                    account.setBalance(account.getBalance().add(transactionDTO.getAmount()));
                    // Convertir Flux<String> a Flux<Transaction>
                    Flux<Transaction> transactions = account.getTransactions()
                            .flatMap(transactionId -> Mono.just(TransactionConverter.transactionDTOToTransaction(transactionDTO)));
                    account.setTransactions(transactions);

                    return accountRepository.save(account)
                            .thenReturn(accountToAccountDTO(account));
                });
    }

    @Override
    public Flux<TransactionDTO> getTransactionsByAccountId(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .map(TransactionConverter::transactionToTransactionDTO);
    }

}
