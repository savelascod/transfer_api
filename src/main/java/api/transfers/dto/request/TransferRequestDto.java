package api.transfers.dto.request;

import api.transfers.constant.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDto {
    @NotNull
    private Integer recipientAccountNumber;

    @NotNull
    private Integer senderAccountNumber;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    private Double amount;

    @NotNull
    @NotEmpty
    private Currency currency;
}
