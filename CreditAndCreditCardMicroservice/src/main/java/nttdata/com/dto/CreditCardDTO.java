package nttdata.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDTO {
    private String id;
    private BigDecimal creditLimit;
    private BigDecimal currentBalance;
    private List<String> transactions;
}