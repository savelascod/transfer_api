package api.transfers.service.impl;

import api.transfers.persistence.entity.AccountEntity;
import api.transfers.persistence.repository.IAccountRepository;
import org.springframework.stereotype.Service;
import api.transfers.service.contract.IAccountService;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    private final IAccountRepository accountRepository;

    public AccountService(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<AccountEntity> findAccountByNumberEquals(Integer accountNumber) {
        return accountRepository.findByNumberEquals(accountNumber);
    }

    @Override
    public Optional<AccountEntity> findAccountById(Integer accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public List<AccountEntity> findAllAccountsByUserId(Integer userId) {
        return accountRepository.findAllByUserIdEquals(userId);
    }

}
