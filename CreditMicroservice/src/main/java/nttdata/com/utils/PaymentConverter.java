package nttdata.com.utils;

import nttdata.com.dto.PaymentDTO;
import nttdata.com.model.Payment;
import nttdata.com.model.Transaction;

public class PaymentConverter {

    public static PaymentDTO paymentToPaymentDTO(Payment entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(entity.getPaymentId());
        dto.setAmount(entity.getAmount());
        dto.setTimestamp(entity.getTimestamp());

        return dto;
    }
    public static Payment paymentDTOToPayment(PaymentDTO dto) {
        Payment entity = new Payment();
        entity.setPaymentId(dto.getPaymentId());
        entity.setAmount(dto.getAmount());
        entity.setTimestamp(dto.getTimestamp());

        return entity;
    }
}