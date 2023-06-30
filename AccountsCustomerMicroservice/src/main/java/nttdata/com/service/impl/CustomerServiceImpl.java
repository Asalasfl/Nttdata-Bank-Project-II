package nttdata.com.service.impl;

import lombok.AllArgsConstructor;
import nttdata.com.dto.AccountDTO;
import nttdata.com.dto.CreditCardDTO;
import nttdata.com.dto.CreditDTO;
import nttdata.com.dto.CustomerDTO;
import nttdata.com.client.feign.CreditCardClient;
import nttdata.com.client.feign.CreditClient;
import nttdata.com.model.Account;
import nttdata.com.model.Credit;
import nttdata.com.model.CreditCard;
import nttdata.com.model.Customer;
import nttdata.com.repository.AccountRepository;
import nttdata.com.repository.CustomerRepository;
import nttdata.com.service.CustomerService;
import nttdata.com.utils.AccountConverter;
import nttdata.com.utils.CreditCardConverter;
import nttdata.com.utils.CreditConverter;
import nttdata.com.utils.CustomerConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    @Autowired
    private CreditClient creditClient;
    @Autowired
    private CreditCardClient creditCardClient;

    @Override
    public Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        Customer customer = CustomerConverter.customerDTOToCustomer(customerDTO);
        return customerRepository.save(customer)
                .map(CustomerConverter::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(String id, CustomerDTO customerDTO) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    customer.setType(customerDTO.getType());

                    Flux<Account> accountIds = customerDTO.getAccounts()
                            .flatMap(accountId -> accountRepository.findById(accountId.get()))
                            .collectList()
                            .flatMapMany(Flux::fromIterable);
                    customer.setAccountIds(accountIds);

                    Flux<Credit> creditIds = customerDTO.getCredits()
                            .flatMap(creditId -> creditClient.findByCreditId(creditId.getIdCredit()))
                            .collectList()
                            .flatMapMany(Flux::fromIterable);
                    customer.setCreditIds(creditIds);

                    Flux<CreditCard> creditCardIds = customerDTO.getCreditCards()
                            .flatMap(creditCardId -> creditCardClient.findByCreditCardId(creditCardId.getIdCreditCard()))
                            .collectList()
                            .flatMapMany(Flux::fromIterable);
                    customer.setCreditCardIds(creditCardIds);

                    return customerRepository.save(customer)
                            .map(CustomerConverter::customerToCustomerDTO);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(String id) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    Flux<AccountDTO> accounts = customer.getAccountIds()
                            .flatMap(account -> accountRepository.findById(account.getAccountId())
                                    .map(AccountConverter::accountToAccountDTO));
                    Flux<CreditDTO> credits = customer.getCreditIds()
                            .flatMap(credit -> creditClient.findByCreditId(credit.getCreditId())
                                    .map(CreditConverter::creditToDTO));
                    Flux<CreditCardDTO> creditCards = customer.getCreditCardIds()
                            .flatMap(creditCard -> creditCardClient.findByCreditCardId(creditCard.getCreditCardId())
                                    .map(CreditCardConverter::creditCardToDTO));

                    return Mono.zip(accounts.collectList(), credits.collectList(), creditCards.collectList())
                            .map(tuple -> new CustomerDTO(
                                    customer.getId(),
                                    customer.getName(),
                                    customer.getType(),
                                    Flux.fromIterable(tuple.getT1()),
                                    Flux.fromIterable(tuple.getT2()),
                                    Flux.fromIterable(tuple.getT3()),
                                    null
                            ));
                });
    }


}