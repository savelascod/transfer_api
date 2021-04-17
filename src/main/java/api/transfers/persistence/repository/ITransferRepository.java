package api.transfers.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import api.transfers.persistence.entity.TransferEntity;

import java.util.List;

@Repository
public interface ITransferRepository extends JpaRepository<TransferEntity, Integer> {
    Page<TransferEntity> findAllBySenderAccountIdEqualsOrRecipientAccountIdEquals(
            Integer senderAccountId, Integer recipientAccountId, Pageable pageable);

    List<TransferEntity> findAllBySenderAccountIdEqualsOrRecipientAccountIdEquals(
            Integer senderAccountId, Integer recipientAccountId);
}
