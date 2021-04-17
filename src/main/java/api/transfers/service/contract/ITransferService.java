package api.transfers.service.contract;

import api.transfers.dto.domain.TransferDto;
import api.transfers.dto.request.GetTransferFilterDto;
import api.transfers.exception.BusinessException;
import api.transfers.persistence.entity.AccountEntity;
import api.transfers.persistence.entity.UserEntity;
import api.transfers.dto.request.TransferRequestDto;

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
