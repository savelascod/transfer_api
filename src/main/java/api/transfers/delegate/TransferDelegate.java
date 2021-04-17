package api.transfers.delegate;


import api.transfers.constant.BusinessError;
import api.transfers.dto.domain.TransferDto;
import api.transfers.dto.request.GetTransferFilterDto;
import api.transfers.exception.BusinessException;
import api.transfers.persistence.entity.AccountEntity;
import api.transfers.persistence.entity.UserEntity;
import api.transfers.service.contract.IAccountService;
import api.transfers.service.contract.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import api.transfers.dto.request.TransferRequestDto;
import api.transfers.service.contract.ITransferService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Component
public class TransferDelegate {

    private final ITransferService transferService;
    private final IUserService userService;
    private final IAccountService accountService;


    @Autowired
    public TransferDelegate(ITransferService transferService,
                            IUserService userService,
                            IAccountService accountService) {
        this.transferService = transferService;
        this.userService = userService;
        this.accountService = accountService;
    }

    public List<BusinessException> performTransfer(TransferRequestDto transferRequestDto) {
        Supplier<Optional<AccountEntity>> senderAccountSupplier = () ->
                accountService.findAccountByNumberEquals(transferRequestDto.getSenderAccountNumber());
        Supplier<Optional<AccountEntity>> recipientAccountSupplier = () ->
                accountService.findAccountByNumberEquals(transferRequestDto.getRecipientAccountNumber());
        BiFunction<Optional<AccountEntity>, Optional<AccountEntity>, List> performValidationsFunction =
                (optionalSenderAccount, optionalRecipientAccount) -> {
                    List<BusinessException> businessErrors = validateSenderAccount(optionalSenderAccount);
                    businessErrors.addAll(validateRecipientAccount(optionalRecipientAccount));
                    return businessErrors;
                };
        return transferService.performTransfer(transferRequestDto, senderAccountSupplier,
                recipientAccountSupplier, performValidationsFunction);
    }

    private List<BusinessException> validateSenderAccount(Optional<AccountEntity> optionalSenderAccount) {
        ArrayList<BusinessException> businessExceptions = new ArrayList<>();
        optionalSenderAccount.ifPresentOrElse(senderAccount -> {
            Optional<UserEntity> senderUserEntity = userService.findUserById(senderAccount.getUserId());
            senderUserEntity.ifPresentOrElse(userEntity -> {
            }, () -> businessExceptions
                    .add(new BusinessException(String.format(BusinessError.USER_NOT_EXIST.getMessage(),
                            senderAccount.getUserId()))));
        }, () -> businessExceptions.add(new BusinessException(
                String.format(BusinessError.SENDER_ACCOUNT_NOT_EXIST.getMessage()))));
        return businessExceptions;
    }

    private List<BusinessException> validateRecipientAccount(Optional<AccountEntity> optionalRecipientAccount) {
        ArrayList<BusinessException> businessExceptions = new ArrayList<>();
        if (!optionalRecipientAccount.isPresent()) businessExceptions.add(new BusinessException(
                String.format(BusinessError.RECIPIENT_ACCOUNT_NOT_EXIST.getMessage())));
        return businessExceptions;
    }

    public List<TransferDto> getAllUserMovements(int page, int size, GetTransferFilterDto transferFilterDto, User currentUser) {
        return transferService.getAllMovements(
                () -> userService.getCurrentUserInSession(currentUser),
                accountId -> accountService.findAccountById(accountId),
                userId -> accountService.findAllAccountsByUserId(userId),
                transferFilterDto,
                page, size);
    }
}
