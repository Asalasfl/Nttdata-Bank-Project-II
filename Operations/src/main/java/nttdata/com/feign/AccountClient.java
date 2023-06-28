package nttdata.com.feign;

import nttdata.com.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@FeignClient(name = "micro-account")
public interface AccountClient {

    @GetMapping("/buscar/dni/{dni}")
    public Mono<AccountDTO> buscarPorDni(@PathVariable("id") String id);
    public Mono<AccountDTO> createAccount(@PathVariable() AccountDTO accountDTO);
}
