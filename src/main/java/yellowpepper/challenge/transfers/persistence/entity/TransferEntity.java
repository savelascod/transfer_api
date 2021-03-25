package yellowpepper.challenge.transfers.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yellowpepper.challenge.transfers.constant.Currency;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Table(name = "transfer")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer recipientAccountId;

    @NotNull
    private Integer senderAccountId;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    private Double amount;

    @NotNull
    private OffsetDateTime date;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Currency currency;
}
