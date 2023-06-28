package nttdata.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String id;
    private String name;
    private String type;
    private List<AccountDTO> accounts;
    private List<CreditDTO> credits;
    private List<CreditCardDTO> creditCards;
}