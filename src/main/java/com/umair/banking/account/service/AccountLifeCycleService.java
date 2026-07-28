package com.umair.banking.account.service;

public interface AccountLifeCycleService {

    void freezeAccount(Long accountId);
    void activateAccount(Long accountId);
    void closeAccount(Long accountId);
}
