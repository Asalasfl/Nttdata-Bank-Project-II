package nttdata.com.service.impl;

import nttdata.com.dto.CreditCardDTO;
import nttdata.com.dto.TransactionDTO;
import nttdata.com.model.CreditCard;
import nttdata.com.model.Transaction;
import nttdata.com.repository.CreditCardRepository;
import nttdata.com.repository.TransactionRepository;
import nttdata.com.service.CreditCardService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, TransactionRepository transactionRepository) {
        this.creditCardRepository = creditCardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Mono<CreditCardDTO> getCreditCardById(String id) {
        return creditCardRepository.findById(id)
                .map(this::mapToCreditCardDTO);
    }

    @Override
    public Mono<CreditCardDTO> addTransaction(String creditCardId, TransactionDTO transactionDTO) {
        return creditCardRepository.findById(creditCardId)
                .flatMap(creditCard -> {
                    creditCard.setCurrentBalance(creditCard.getCurrentBalance().add(transactionDTO.getAmount()));
                    creditCard.getTransactions().add(transactionDTO.getId());
                    return creditCardRepository.save(creditCard)
                            .thenReturn(mapToCreditCardDTO(creditCard));
                });
    }

    @Override
    public Flux<TransactionDTO> getTransactionsByCreditCardId(String creditCardId) {
        return transactionRepository.findByCreditCardId(creditCardId)
                .map(this::mapToTransactionDTO);
    }

    private CreditCardDTO mapToCreditCardDTO(CreditCard creditCard) {
        return new CreditCardDTO(creditCard.getId(), creditCard.getCreditLimit(),
                creditCard.getCurrentBalance(), creditCard.getTransactions());
    }

    private TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getType(),
                transaction.getAmount(), transaction.getTimestamp());
    }
}
