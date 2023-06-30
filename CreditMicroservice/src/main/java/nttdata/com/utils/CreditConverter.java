package nttdata.com.utils;

import nttdata.com.dto.CreditDTO;
import nttdata.com.dto.PaymentDTO;
import nttdata.com.model.Credit;
import nttdata.com.model.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static nttdata.com.utils.PaymentConverter.paymentToPaymentDTO;

public class CreditConverter {

    public static CreditDTO creditToDTO(Credit entity) {
        CreditDTO dto = new CreditDTO();
        dto.setCreditId(entity.getCreditId());
        dto.setAmount(entity.getAmount());
        dto.setInterestRate(entity.getInterestRate());
        // Convertir Flux<Payment> a Flux<PaymentDTO>
        Flux<PaymentDTO> paymentsDTOs = entity.getPayments()
                .flatMap(payment -> Mono.just(paymentToPaymentDTO(payment)));

        dto.setPayments(paymentsDTOs);
        dto.setRemainingAmount(entity.getRemainingAmount());
        return dto;
    }

    public static Credit DTOToCredit(CreditDTO dto) {
        Credit entity = new Credit();
        entity.setCreditId(dto.getCreditId());
        entity.setAmount(dto.getAmount());
        entity.setInterestRate(dto.getInterestRate());
        entity.setRemainingAmount(dto.getRemainingAmount());


        return entity;
    }


}