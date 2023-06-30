package nttdata.com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    @DBRef
    private Account idAccount;
    @DBRef
    private CreditCard creditCardId;
    private String type; // "DEPOSIT" or "WITHDRAWAL"
    private BigDecimal amount;
    private LocalDateTime timestamp;
}