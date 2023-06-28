package nttdata.com.controller;


import nttdata.com.dto.CreditCardDTO;
import nttdata.com.dto.TransactionDTO;
import nttdata.com.service.impl.CreditCardServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/credit-cards")
public class CreditCardController {
    private final CreditCardServiceImpl creditCardServiceImpl;

    public CreditCardController(CreditCardServiceImpl creditCardServiceImpl) {
        this.creditCardServiceImpl = creditCardServiceImpl;
    }

    @GetMapping(value = "/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CreditCardDTO> getCreditCardById(@PathVariable String id) {
        return creditCardServiceImpl.getCreditCardById(id);
    }

    @PostMapping(value = "/{creditCardId}/transactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CreditCardDTO> addTransaction(@PathVariable String creditCardId,
                                              @RequestBody TransactionDTO transactionDTO) {
        return creditCardServiceImpl.addTransaction(creditCardId, transactionDTO);
    }

    @GetMapping(value = "/{creditCardId}/transactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TransactionDTO> getTransactionsByCreditCardId(@PathVariable String creditCardId) {
        return creditCardServiceImpl.getTransactionsByCreditCardId(creditCardId);
    }
}