package com.mendes.message.functions;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mendes.message.dto.AccountsMessageDto;

@Configuration
public class MessageFunctions {

    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<AccountsMessageDto, AccountsMessageDto> email() {
        return AccountsMessageDto -> {
            log.info("Sending email with the details: {}", AccountsMessageDto.toString());
            return AccountsMessageDto;
        };
    }

    @Bean
    public Function<AccountsMessageDto, Long> sms() {
        return AccountsMessageDto -> {
            log.info("Sending SMS with the details: {}", AccountsMessageDto.toString());
            return AccountsMessageDto.accountNumber();
        };
    }
}
