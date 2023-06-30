package nttdata.com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credits")

/** A credit is a generic concept that can have different types, such as personal credit, business credit
 * establish a common foundation for all bank accounts and allow each account type to implement its own
 * specific behavior, providing flexibility and modularity
 * PERSONAL, ENTERPRISE for String type
 */
public  class Credit {
    @Id
    private String id;
    @DBRef
    @Field("id")
    private Customer customerId;
    private String type;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private BigDecimal remainingAmount;
    @DBRef
    private Flux<Payment> payments; // List of payment IDs
}