package nttdata.com.service.impl;

import lombok.AllArgsConstructor;
import nttdata.com.dto.CreditCardDTO;
import nttdata.com.dto.TransactionDTO;
import nttdata.com.model.Transaction;
import nttdata.com.repository.CreditCardRepository;
import nttdata.com.repository.TransactionRepository;
import nttdata.com.service.CreditCardService;
import nttdata.com.utils.CreditCardConverter;
import nttdata.com.utils.TransactionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@AllArgsConstructor
@Service
public class CreditCardServiceImpl implements CreditCardService {
    
    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<CreditCardDTO> findByCreditCardId(String creditCardId) {
        return creditCardRepository.findById(creditCardId)
                .map(CreditCardConverter::creditCardToDTO)
                .switchIfEmpty(Mono.just(new CreditCardDTO("La tarjeta de crédito no existe.")));
    }
    @Override
    public Mono<CreditCardDTO> addTransaction(String creditCardId, TransactionDTO transactionDTO) {
        return creditCardRepository.findById(creditCardId)
                .flatMap(creditCard -> {
                    creditCard.setCurrentBalance(creditCard.getCurrentBalance().add(transactionDTO.getAmount()));
                    Transaction transaction = TransactionConverter.transactionDTOToTransaction(transactionDTO);

                    // Convertir Flux<Transaction> a List<Transaction> usando collectList()
                    return creditCard.getTransactions()
                            .collectList()
                            .flatMap(transactions -> {
                                transactions.add(transaction); // Agregar la transacción a la lista
                                creditCard.setTransactions(Flux.fromIterable(transactions)); // Actualizar el Flux<Transaction>
                                return creditCardRepository.save(creditCard)
                                        .thenReturn(CreditCardConverter.creditCardToDTO(creditCard));
                            });
                });
    }

    @Override
    public Flux<TransactionDTO> getTransactionsByCreditCardId(String creditCardId) {
        return transactionRepository.findByCreditCardId(creditCardId)
                .map(TransactionConverter::transactionToTransactionDTO);
    }

}
