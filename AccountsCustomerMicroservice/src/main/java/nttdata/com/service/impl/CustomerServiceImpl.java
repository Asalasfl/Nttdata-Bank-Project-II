package nttdata.com.service.impl;

import lombok.AllArgsConstructor;
import nttdata.com.dto.AccountDTO;
import nttdata.com.dto.CreditCardDTO;
import nttdata.com.dto.CreditDTO;
import nttdata.com.dto.CustomerDTO;
import nttdata.com.feign.CreditCardClient;
import nttdata.com.feign.CreditClient;
import nttdata.com.model.Account;
import nttdata.com.model.Credit;
import nttdata.com.model.CreditCard;
import nttdata.com.model.Customer;
import nttdata.com.repository.AccountRepository;
import nttdata.com.repository.CustomerRepository;
import nttdata.com.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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


    public Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        Customer customer = mapToCustomer(customerDTO);
        return customerRepository.save(customer)
                .map(this::mapToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(String id, CustomerDTO customerDTO) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    customer.setType(customerDTO.getType());
                    List<String> accountIds = customerDTO.getAccounts().stream()
                            .map(AccountDTO::getId)
                            .collect(Collectors.toList());
                    customer.setAccountIds(accountIds);
                    List<String> creditsIds = customerDTO.getCredits().stream()
                            .map(CreditDTO::getId)
                            .collect(Collectors.toList());
                    customer.setAccountIds(creditsIds);
                    List<String> creditsCardIds = customerDTO.getCreditCards().stream()
                            .map(CreditCardDTO::getId)
                            .collect(Collectors.toList());
                    customer.setAccountIds(creditsCardIds);

                    return customerRepository.save(customer)
                            .map(this::mapToCustomerDTO);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }
    @Override
    public Mono<CustomerDTO> getCustomerById(String id) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    Mono<List<AccountDTO>> accounts = accountRepository.findByCustomerId(customer.getId())
                            .map(this::mapToAccountDTO)
                            .collectList();
                    Mono<List<CreditDTO>> credits = creditClient.findByCustomerId(customer.getId())
                            .map(this::mapToCreditDTO)
                            .collectList();
                    Mono<List<CreditCardDTO>> creditCards = creditCardClient.findByCustomerId(customer.getId())
                            .map(this::mapToCreditCardDTO)
                            .collectList();
                    return Mono.zip(accounts, credits, creditCards)
                            .map(tuple -> new CustomerDTO(customer.getId(), customer.getName(), customer.getType(),
                                    tuple.getT1(), tuple.getT2(), tuple.getT3()));
                });
    }

    private Customer mapToCustomer(CustomerDTO customerDTO) {
        return new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getType(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    private CustomerDTO mapToCustomerDTO(Customer customer) {
        ModelMapper modelMapper = new ModelMapper();
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;
    }
    private AccountDTO mapToAccountDTO(Account account) {
        return new AccountDTO(account.getId(), account.getType(), account.getBalance(), account.getTransactions());
    }

    private CreditDTO mapToCreditDTO(Credit credit) {
        return new CreditDTO(credit.getId(), credit.getAmount(), credit.getInterestRate(),
                credit.getRemainingAmount(), credit.getPayments());
    }

    private CreditCardDTO mapToCreditCardDTO(CreditCard creditCard) {
        return new CreditCardDTO(creditCard.getId(), creditCard.getCreditLimit(),
                creditCard.getCurrentBalance(), creditCard.getTransactions());
    }
}
