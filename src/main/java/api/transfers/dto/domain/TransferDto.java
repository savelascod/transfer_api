package api.transfers.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferDto {
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
    private OffsetDateTime date;

    @NotNull
    @NotEmpty
    private String originalCurrency;
}
