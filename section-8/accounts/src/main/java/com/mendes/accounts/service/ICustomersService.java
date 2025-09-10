package com.mendes.accounts.service;

import com.mendes.accounts.dto.CustomerDetailsDto;

public interface ICustomersService {
    
    /**
     * 
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
