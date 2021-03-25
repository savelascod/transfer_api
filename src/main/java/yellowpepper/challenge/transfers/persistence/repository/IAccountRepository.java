package yellowpepper.challenge.transfers.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yellowpepper.challenge.transfers.persistence.entity.AccountEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findByNumberEquals(Integer accountNumber);

    List<AccountEntity> findAllByUserIdEquals(Integer userId);
}
