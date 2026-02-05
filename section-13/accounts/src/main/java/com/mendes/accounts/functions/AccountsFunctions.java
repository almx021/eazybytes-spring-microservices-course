package com.mendes.accounts.functions;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mendes.accounts.service.IAccountsService;

@Configuration
public class AccountsFunctions {

    private static final Logger log = LoggerFactory.getLogger(AccountsFunctions.class);

    @Bean
    public Consumer<Long> updateCommunication(IAccountsService accountsService) {
        return AccountNumber -> {
            log.info("Updating communication status for account number: {}", AccountNumber.toString());
            accountsService.updateCommunicationStatus(AccountNumber);
        };
    }
}
