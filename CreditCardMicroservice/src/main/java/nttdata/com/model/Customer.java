package nttdata.com.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Flux;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class Customer {
    @Id
    private String customerId;
    private String name;
    private String type; // "PERSONAL" or "BUSINESS"
    @DBRef
    private Flux<Account> accountIds;
    @DBRef
    private Flux<Credit> creditIds;
    @DBRef
    private Flux<CreditCard> creditCardIds;
}
