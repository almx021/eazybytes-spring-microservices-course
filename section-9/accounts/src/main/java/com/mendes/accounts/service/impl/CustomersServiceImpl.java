package com.mendes.accounts.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mendes.accounts.dto.AccountsDto;
import com.mendes.accounts.dto.CardsDto;
import com.mendes.accounts.dto.CustomerDetailsDto;
import com.mendes.accounts.dto.LoansDto;
import com.mendes.accounts.entity.Accounts;
import com.mendes.accounts.entity.Customer;
import com.mendes.accounts.exception.ResourceNotFoundException;
import com.mendes.accounts.mapper.AccountsMapper;
import com.mendes.accounts.mapper.CustomerMapper;
import com.mendes.accounts.repository.AccountsRepository;
import com.mendes.accounts.repository.CustomerRepository;
import com.mendes.accounts.service.ICustomersService;
import com.mendes.accounts.service.client.CardsFeignClient;
import com.mendes.accounts.service.client.LoansFeignClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * 
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        
        ResponseEntity<CardsDto> cardsDResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        customerDetailsDto.setCardsDto(cardsDResponseEntity.getBody());
        
        return customerDetailsDto;
    }
    
    
}
