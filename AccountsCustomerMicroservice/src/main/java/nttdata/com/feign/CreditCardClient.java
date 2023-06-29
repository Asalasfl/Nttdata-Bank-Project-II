package nttdata.com.feign;

import nttdata.com.dto.CreditCardDTO;
import nttdata.com.dto.CustomerDTO;
import nttdata.com.model.CreditCard;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;

@FeignClient(name = "micro-credit-card")
public interface CreditCardClient {

    @GetMapping("/api/credit-cards/{id}")
    public Flux<CreditCard> findByCreditCardId(@PathVariable("id") String id);
}
