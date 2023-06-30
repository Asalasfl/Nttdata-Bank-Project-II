package nttdata.com.utils;

import nttdata.com.dto.AccountDTO;
import nttdata.com.dto.TransactionDTO;
import nttdata.com.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static nttdata.com.utils.TransactionConverter.transactionToTransactionDTO;

public class AccountConverter {

    public static AccountDTO accountToAccountDTO(Account entity) {
        AccountDTO dto = new AccountDTO();
        dto.setIdAccount(entity.getAccountId());
        dto.setType(entity.getType());
        dto.setBalance(entity.getBalance());
        // Convertir Flux<Transaction> a Flux<TransactionDTO>
        Flux<TransactionDTO> transactionDTOs = entity.getTransactions()
                .flatMap(transaction -> Mono.just(transactionToTransactionDTO(transaction)));

        dto.setTransactions(transactionDTOs);

        return dto;
    }

    public static Account accountDTOToAccount(AccountDTO dto) {
        Account entity = new Account();
        entity.setAccountId(dto.getIdAccount());
        entity.setType(dto.getType());
        entity.setBalance(dto.getBalance());

        return entity;
    }
}