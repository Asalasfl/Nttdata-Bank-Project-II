package nttdata.com.utils;

import nttdata.com.dto.*;
import nttdata.com.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static nttdata.com.utils.AccountConverter.accountToAccountDTO;
import static nttdata.com.utils.CreditCardConverter.creditCardToDTO;
import static nttdata.com.utils.CreditConverter.creditToDTO;
import static nttdata.com.utils.TransactionConverter.transactionToTransactionDTO;

public class    CustomerConverter {

    // Metodo que convierte un Customer Entity a un Customer Dto
    public static CustomerDTO customerToCustomerDTO(Customer entity){
        CustomerDTO dto = new CustomerDTO();
        dto.setIdCustomer(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        // Convertir Flux<Account> a Flux<AccountDTO>
        Flux<AccountDTO> accountsDTOs = entity.getAccountIds()
                .flatMap(account -> Mono.just(accountToAccountDTO(account)));
        dto.setAccounts(accountsDTOs);

        // Convertir Flux<Credit> a Flux<CreditDTO>
        Flux<CreditDTO> creditsDTOs = entity.getCreditIds()
                .flatMap(credit -> Mono.just(creditToDTO(credit)));
        dto.setCredits(creditsDTOs);

        // Convertir Flux<CreditCard> a Flux<CreditCardDTO>
        Flux<CreditCardDTO> creditCardsDTOs = entity.getCreditCardIds()
                .flatMap(creditCard -> Mono.just(creditCardToDTO(creditCard)));
        dto.setCreditCards(creditCardsDTOs);
        return dto;
    }

    // Metodo que convierte un Customer Dto a un Customer Entity
    public static Customer customerDTOToCustomer(CustomerDTO dto) {
        Customer entity = new Customer();
        entity.setId(dto.getIdCustomer());
        entity.setName(dto.getName());
        entity.setType(dto.getType());

        // No se realiza la conversión de Flux<String> a List<String> porque en Customer no se utiliza una lista directamente

        return entity;
    }
}