package nttdata.com.controller;

import nttdata.com.dto.CreditDTO;
import nttdata.com.dto.PaymentDTO;
import nttdata.com.service.impl.CreditServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/credits")
public class CreditController {
    private final CreditServiceImpl creditServiceImpl;

    public CreditController(CreditServiceImpl creditServiceImpl) {
        this.creditServiceImpl = creditServiceImpl;
    }

    @GetMapping(value = "/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CreditDTO> findCreditById(@PathVariable String id) {
        return creditServiceImpl.findByCreditId(id);
    }
    @PostMapping(value = "/{creditId}/payments", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CreditDTO> addPayment(@PathVariable String creditId,
                                      @RequestBody PaymentDTO paymentDTO) {
        return creditServiceImpl.addPayment(creditId, paymentDTO);
    }

    @GetMapping(value = "/{creditId}/payments", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PaymentDTO> getPaymentsByCreditId(@PathVariable String creditId) {
        return creditServiceImpl.getPaymentsByCreditId(creditId);
    }
}