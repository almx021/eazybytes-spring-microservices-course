package com.mendes.accounts.service;

import com.mendes.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     *
     * @param customerDto - CustomerDto Object
     */
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String mobileNumber);

    /**
     * Updates the communication status for an account.
     *
     * @param accountNumber - Account Number for which the communication status needs to be updated
     * @return {@code true} if the communication status was successfully updated, {@code false} otherwise
     */
    boolean updateCommunicationStatus(Long accountNumber);

}
