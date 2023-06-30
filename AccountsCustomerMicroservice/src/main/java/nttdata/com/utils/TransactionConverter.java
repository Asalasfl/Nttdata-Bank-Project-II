package nttdata.com.utils;

import nttdata.com.dto.TransactionDTO;
import nttdata.com.model.Transaction;

public class TransactionConverter {

    public static TransactionDTO transactionToTransactionDTO(Transaction entity) {
        TransactionDTO dto = new TransactionDTO();
        dto.setIdTransaction(entity.getTransactionId());
        dto.setType(entity.getType());
        dto.setAmount(entity.getAmount());
        dto.setTimestamp(entity.getTimestamp());

        return dto;
    }
    public static Transaction transactionDTOToTransaction(TransactionDTO dto) {
        Transaction entity = new Transaction();
        entity.setTransactionId(dto.getIdTransaction());
        entity.setType(dto.getType());
        entity.setAmount(dto.getAmount());
        entity.setTimestamp(dto.getTimestamp());

        return entity;
    }
}