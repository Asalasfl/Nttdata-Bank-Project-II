package nttdata.com.service.impl;

import lombok.AllArgsConstructor;
import nttdata.com.dto.AccountDTO;
import nttdata.com.service.AccountService;
import nttdata.com.service.CustomerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Service
public class CustomerServiceMediator {

    private final CustomerService customerService;
    private final AccountService accountService;

    public Mono<Void> addAccountToCustomer(String customerId, AccountDTO accountDTO) {
        return customerService.getCustomerById(customerId)
                .flatMap(customer -> {
                    if (customer.getType().equals("PERSONAL")) {
                        // Verificar si el cliente ya tiene una cuenta del mismo tipo SAVING, CHECKING
                        if (customer.getAccounts().stream().noneMatch(c -> c.getType().equals(accountDTO.getType()))) {
                            customer.getAccounts().add(accountDTO);
                            return accountService.createAccount(accountDTO)
                                    .then(customerService.updateCustomer(customerId, customer));
                        } else {
                            return Mono.error(new RuntimeException("El cliente personal ya tiene una cuenta del mismo tipo."));
                        }
                    } else {
                        return Mono.error(new RuntimeException("No se puede agregar una cuenta a un cliente empresarial."));
                    }
                }).then();
    }
}
