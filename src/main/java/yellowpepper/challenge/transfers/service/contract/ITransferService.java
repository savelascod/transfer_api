package yellowpepper.challenge.transfers.service.contract;

import yellowpepper.challenge.transfers.dto.domain.TransferDto;
import yellowpepper.challenge.transfers.dto.request.GetTransferFilterDto;
import yellowpepper.challenge.transfers.dto.request.TransferRequestDto;
import yellowpepper.challenge.transfers.exception.BusinessException;
import yellowpepper.challenge.transfers.persistence.entity.AccountEntity;
import yellowpepper.challenge.transfers.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ITransferService {

    List<BusinessException> performTransfer(
            TransferRequestDto transferRequestDto,
            Supplier<Optional<AccountEntity>> senderAccountSupplier,
            Supplier<Optional<AccountEntity>> recipientAccountSupplier,
            BiFunction<Optional<AccountEntity>, Optional<AccountEntity>, List> performValidationsFunction);

    List<TransferDto> getAllMovements(
            Supplier<Optional<UserEntity>> currentUserEntity,
            Function<Integer, Optional<AccountEntity>> getAccountById,
            Function<Integer, List<AccountEntity>> getUserAccounts,
            GetTransferFilterDto transferFilterDto, int page, int size);
}
