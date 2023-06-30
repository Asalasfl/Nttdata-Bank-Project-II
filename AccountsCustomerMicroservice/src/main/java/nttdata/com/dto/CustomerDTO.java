package nttdata.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String idCustomer;
    private String name;
    private String type;
    private List<String> accounts;
    private List<String> credits;
    private List<String> creditCards;
}