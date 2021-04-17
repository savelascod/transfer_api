package api.transfers.service.contract;

import api.transfers.persistence.entity.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    Optional<AccountEntity> findAccountByNumberEquals(Integer accountNumber);

    Optional<AccountEntity> findAccountById(Integer accountId);

    List<AccountEntity> findAllAccountsByUserId(Integer userId);
}
