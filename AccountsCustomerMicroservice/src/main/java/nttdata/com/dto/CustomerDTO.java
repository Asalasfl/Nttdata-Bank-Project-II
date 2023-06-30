package nttdata.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
    private String idCustomer;
    private String name;
    private String type;
    private Flux<List<AccountDTO>> accounts;
    private Flux<List<CreditDTO>> credits;
    private Flux<List<CreditCardDTO>> creditCards;
    private String messageDto;

    public CustomerDTO(String messageDto){
        this.setMessageDto(messageDto);
    }
}