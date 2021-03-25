package yellowpepper.challenge.transfers.service.impl;

import org.springframework.stereotype.Service;
import yellowpepper.challenge.transfers.persistence.entity.AccountEntity;
import yellowpepper.challenge.transfers.persistence.repository.IAccountRepository;
import yellowpepper.challenge.transfers.service.contract.IAccountService;

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
