package nttdata.com.utils;

import nttdata.com.dto.CreditCardDTO;
import nttdata.com.dto.TransactionDTO;
import nttdata.com.model.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static nttdata.com.utils.TransactionConverter.transactionToTransactionDTO;


public class    CreditCardConverter {

    public static CreditCardDTO creditCardToDTO(CreditCard entity) {
        CreditCardDTO dto = new CreditCardDTO();
        dto.setCreditCardId(entity.getCreditCardId());
        dto.setCreditLimit(entity.getCreditLimit());
        dto.setCurrentBalance(entity.getCurrentBalance());
        // Convertir Flux<Transaction> a Flux<TransactionDTO>
        Flux<TransactionDTO> transactionDTOs = entity.getTransactions()
                .flatMap(transaction -> Mono.just(transactionToTransactionDTO(transaction)));

        dto.setTransactions(transactionDTOs);
        return dto;
    }

    public static CreditCard DTOToCreditCard(CreditCardDTO dto) {
        CreditCard entity = new CreditCard();
        entity.setCreditCardId(dto.getCreditCardId());
        entity.setCreditLimit(dto.getCreditLimit());
        entity.setCurrentBalance(dto.getCurrentBalance());


        return entity;
    }


}