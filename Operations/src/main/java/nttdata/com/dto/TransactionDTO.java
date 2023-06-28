package nttdata.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** type = deposit, withdrawal
 *
 */
public class TransactionDTO {
    private String id;
    private String type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}