package api.transfers.service.impl;

import api.transfers.dto.request.GetTransferFilterDto;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import api.transfers.client.IExchangeApiFeignClient;
import api.transfers.constant.BusinessError;
import api.transfers.constant.Currency;
import api.transfers.dto.domain.TransferDto;
import api.transfers.dto.request.TransferRequestDto;
import api.transfers.dto.response.ExchangeApiResponseDto;
import api.transfers.exception.BusinessException;
import api.transfers.persistence.entity.AccountEntity;
import api.transfers.persistence.entity.TransferEntity;
import api.transfers.persistence.entity.UserEntity;
import api.transfers.persistence.repository.ITransferRepository;
import api.transfers.service.contract.ITransferService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.vavr.API.*;

@Service
public class TransferService implements ITransferService {

    private final IExchangeApiFeignClient exchangeApiFeignClient;
    private final ITransferRepository transferRepository;

    @Autowired
    public TransferService(IExchangeApiFeignClient exchangeApiFeignClient, ITransferRepository transferRepository) {
        this.exchangeApiFeignClient = exchangeApiFeignClient;
        this.transferRepository = transferRepository;
    }

    @Override
    public List<BusinessException> performTransfer(
            TransferRequestDto transferRequestDto,
            Supplier<Optional<AccountEntity>> senderAccountSupplier,
            Supplier<Optional<AccountEntity>> recipientAccountSupplier,
            BiFunction<Optional<AccountEntity>, Optional<AccountEntity>, List> performValidationsFunction) {
        Optional<AccountEntity> optionalSenderAccount = senderAccountSupplier.get();
        Optional<AccountEntity> optionalRecipientAccount = recipientAccountSupplier.get();
        List<BusinessException> businessErrors = performValidationsFunction.apply(optionalSenderAccount, optionalRecipientAccount);
        if (businessErrors.isEmpty()) {
            transformCurrency(transferRequestDto);
            transferRepository.save(TransferEntity.builder()
                    .description(transferRequestDto.getDescription())
                    .amount(transferRequestDto.getAmount())
                    .currency(transferRequestDto.getCurrency())
                    .recipientAccountId(optionalRecipientAccount.get().getId())
                    .senderAccountId(optionalSenderAccount.get().getId())
                    .date(OffsetDateTime.now())
                    .build());
        }
        return businessErrors;
    }

    @Override
    public List<TransferDto> getAllMovements(
            Supplier<Optional<UserEntity>> currentUserEntity,
            Function<Integer, Optional<AccountEntity>> getAccountById,
            Function<Integer, List<AccountEntity>> getUserAccounts,
            GetTransferFilterDto transferFilterDto, int page, int size) {
        return Try(() -> {
            List<TransferDto> movements = new ArrayList<>();
            getUserAccounts.apply(currentUserEntity.get().get().getId()).forEach(accountEntity ->
                    movements.addAll(transferRepository.findAllBySenderAccountIdEqualsOrRecipientAccountIdEquals
                            (accountEntity.getId(), accountEntity.getId(), buildPaginationFilter(transferFilterDto, page, size))
                            .getContent().stream().map(transferEntity -> TransferDto.builder()
                                    .amount(transferEntity.getAmount())
                                    .date(transferEntity.getDate())
                                    .description(transferEntity.getDescription())
                                    .originalCurrency(transferEntity.getCurrency().name())
                                    .recipientAccountNumber(getAccountById.apply(transferEntity
                                            .getRecipientAccountId()).get().getNumber())
                                    .senderAccountNumber(getAccountById.apply(transferEntity
                                            .getSenderAccountId()).get().getNumber())
                                    .build()).collect(Collectors.toList())));
            return movements;
        }).toEither().fold(exception -> new ArrayList<>(), response -> response);
    }

    private List<BusinessException> transformCurrency(TransferRequestDto transferRequestDto) {
        return Try(() -> {
            ExchangeApiResponseDto exchangeApiResponseDto = exchangeApiFeignClient.getExchanges(Currency.USD.name());
            if (!transferRequestDto.getCurrency().equals(Currency.USD)) {
                transferRequestDto.setCurrency(Currency.USD);
                transferRequestDto.setAmount(
                        transferRequestDto.getAmount()
                                / exchangeApiResponseDto.getRates().get(transferRequestDto.getCurrency().name()));
            }
            return transferRequestDto;
        }).toEither().fold(exception -> {
            ArrayList<BusinessException> conversionErrors = new ArrayList<>();
            conversionErrors.add(new BusinessException(
                    String.format(BusinessError.CURRENCY_CONVERSION_ERROR.getMessage())));
            return conversionErrors;
        }, response -> new ArrayList<>());
    }

    private Pageable buildPaginationFilter(GetTransferFilterDto transferFilterDto, int page, int size) {
        Tuple2<Boolean, Boolean> filters = Tuple(transferFilterDto.getAmountAscendant(), transferFilterDto.getDateAscendant());
        return Match(filters).of(
                Case($(Tuple.of(true, true)), () ->
                        PageRequest.of(page, size, Sort.by("date").ascending().and(Sort.by("amount").ascending()))),
                Case($(Tuple.of(true, false)), () ->
                        PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("amount").ascending()))),
                Case($(Tuple.of(false, true)), () ->
                        PageRequest.of(page, size, Sort.by("date").ascending().and(Sort.by("amount").descending()))),
                Case($(Tuple.of(false, false)), () ->
                        PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("amount").descending()))),
                Case($(Tuple.of(true, null)), () ->
                        PageRequest.of(page, size, Sort.by("amount").ascending())),
                Case($(Tuple.of(false, null)), () ->
                        PageRequest.of(page, size, Sort.by("amount").descending())),
                Case($(Tuple.of(null, true)), () ->
                        PageRequest.of(page, size, Sort.by("date").ascending())),
                Case($(Tuple.of(null, false)), () ->
                        PageRequest.of(page, size, Sort.by("date").descending())),
                Case($(), () -> PageRequest.of(page, size))
        );
    }
}
