package nttdata.com.service.impl;

import lombok.AllArgsConstructor;
import nttdata.com.dto.CreditDTO;
import nttdata.com.dto.PaymentDTO;
import nttdata.com.model.Credit;
import nttdata.com.model.Payment;
import nttdata.com.model.Transaction;
import nttdata.com.repository.CreditRepository;
import nttdata.com.repository.PaymentRepository;
import nttdata.com.service.CreditService;
import nttdata.com.utils.CreditConverter;
import nttdata.com.utils.PaymentConverter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final PaymentRepository paymentRepository;


    @Override
    public Mono<CreditDTO> findByCreditId(String customerId) {
        return creditRepository.findById(customerId)
                .map(CreditConverter::creditToDTO)
                .map(Mono::just)
                .blockOptional().orElseGet(() -> Mono.just(new CreditDTO("El crédito no existe.")));
    }


    @Override
    public Mono<CreditDTO> addPayment(String creditId, PaymentDTO paymentDTO) {
        return creditRepository.findById(creditId)
                .flatMap(credit -> {
                    credit.setRemainingAmount(credit.getRemainingAmount().subtract(paymentDTO.getAmount()));
                    Payment payment = PaymentConverter.paymentDTOToPayment(paymentDTO);

                    // Convertir Flux<Transaction> a List<Transaction> usando collectList()
                    return credit.getPayments()
                            .collectList()
                            .flatMap(payments -> {
                                payments.add(payment); // Agregar el pago a la lista
                                credit.setPayments(Flux.fromIterable(payments)); // Actualizar el Flux<Transaction>
                                return creditRepository.save(credit)
                                        .thenReturn(CreditConverter.creditToDTO(credit));
                            });
                });
    }

    @Override
    public Flux<PaymentDTO> getPaymentsByCreditId(String creditId) {
        return paymentRepository.findByCreditId(creditId)
                .map(PaymentConverter::paymentToPaymentDTO);
    }

}
