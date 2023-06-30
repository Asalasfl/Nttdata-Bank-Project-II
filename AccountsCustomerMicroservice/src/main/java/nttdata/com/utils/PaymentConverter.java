package nttdata.com.utils;

import nttdata.com.dto.PaymentDTO;
import nttdata.com.model.Payment;

public class PaymentConverter {

    public static PaymentDTO paymentToPaymentDTO(Payment entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setIdPayment(entity.getPaymentId());
        dto.setAmount(entity.getAmount());
        dto.setTimestamp(entity.getTimestamp());

        return dto;
    }
    public static Payment paymentDTOToPayment(PaymentDTO dto) {
        Payment entity = new Payment();
        entity.setPaymentId(dto.getIdPayment());
        entity.setAmount(dto.getAmount());
        entity.setTimestamp(dto.getTimestamp());

        return entity;
    }
}