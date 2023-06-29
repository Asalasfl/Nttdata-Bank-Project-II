package nttdata.com.service.impl;

import nttdata.com.dto.CreditDTO;
import nttdata.com.dto.PaymentDTO;
import nttdata.com.model.Credit;
import nttdata.com.model.Payment;
import nttdata.com.repository.CreditRepository;
import nttdata.com.repository.PaymentRepository;
import nttdata.com.service.CreditService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final PaymentRepository paymentRepository;

    public CreditServiceImpl(CreditRepository creditRepository, PaymentRepository paymentRepository) {
        this.creditRepository = creditRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Mono<CreditDTO> findByCreditId(String customerId) {
       return  creditRepository.findById(customerId)
                .map(this::mapToCreditDTO);
    }


    @Override
    public Mono<CreditDTO> addPayment(String creditId, PaymentDTO paymentDTO) {
        return creditRepository.findById(creditId)
                .flatMap(credit -> {
                    credit.setRemainingAmount(credit.getRemainingAmount().subtract(paymentDTO.getAmount()));
                    credit.getPayments().add(paymentDTO.getId());
                    return creditRepository.save(credit)
                            .thenReturn(mapToCreditDTO(credit));
                });
    }

    @Override
    public Flux<PaymentDTO> getPaymentsByCreditId(String creditId) {
        return paymentRepository.findByCreditId(creditId)
                .map(this::mapToPaymentDTO);
    }

    private CreditDTO mapToCreditDTO(Credit credit) {
        return new CreditDTO(credit.getId(), credit.getAmount(), credit.getInterestRate(),
                credit.getRemainingAmount(), credit.getPayments());
    }

    private PaymentDTO mapToPaymentDTO(Payment payment) {
        return new PaymentDTO(payment.getId(), payment.getAmount(), payment.getTimestamp());
    }
}
