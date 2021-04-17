package api.transfers.persistence.repository;

import api.transfers.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findByNumberEquals(Integer accountNumber);

    List<AccountEntity> findAllByUserIdEquals(Integer userId);
}
