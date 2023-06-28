package nttdata.com.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String name;
    private String type; // "PERSONAL" or "BUSINESS"
    private List<Account> accountIds;
    private List<Credit> creditIds;
    private List<CreditCard> creditCardIds;
}
