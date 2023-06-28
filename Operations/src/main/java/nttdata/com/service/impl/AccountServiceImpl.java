package nttdata.com.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nttdata.com.dto.AccountDTO;
import nttdata.com.dto.TransactionDTO;
import nttdata.com.model.Account;
import nttdata.com.model.Transaction;
import nttdata.com.repository.AccountRepository;
import nttdata.com.repository.CustomerRepository;
import nttdata.com.repository.TransactionRepository;
import nttdata.com.service.AccountService;
import nttdata.com.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;




    @Override
    public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
        Account account = new Account();
            account.setId(accountDTO.getId());
            account.setType(accountDTO.getType());
            account.setBalance(accountDTO.getBalance());
            account.setTransactions(accountDTO.getTransactions());
             return accountRepository.save(account)
                .map(this::mapToAccountDTO);
    }
    @Override
    public Mono<AccountDTO> getAccountById(String id) {
        return accountRepository.findById(id)
                .map(this::mapToAccountDTO);
    }

    @Override
    public Mono<AccountDTO> updateAccount(String id, AccountDTO accountDTO) {
        return accountRepository.findById(id)
                .flatMap(account -> {
                    account.setType(accountDTO.getType());
                    account.setBalance(accountDTO.getBalance());
                    account.setTransactions(accountDTO.getTransactions());

                    return accountRepository.save(account)
                            .map(this::mapToAccountDTO);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }
    @Override
    public Mono<AccountDTO> addTransaction(String accountId, TransactionDTO transactionDTO) {
        return accountRepository.findById(accountId)
                .flatMap(account -> {
                    account.setBalance(account.getBalance().add(transactionDTO.getAmount()));
                    account.getTransactions().add(transactionDTO.getId());
                    return accountRepository.save(account)
                            .thenReturn(mapToAccountDTO(account));
                });
    }

    @Override
    public Flux<TransactionDTO> getTransactionsByAccountId(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .map(this::mapToTransactionDTO);
    }

    private AccountDTO mapToAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setType(account.getType());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setTransactions(account.getTransactions());
        return accountDTO;
    }
    private TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getType(),
                transaction.getAmount(), transaction.getTimestamp());
    }
}
