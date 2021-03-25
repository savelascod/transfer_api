package yellowpepper.challenge.transfers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yellowpepper.challenge.transfers.dto.domain.TransferDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTransfersResponseDto {
    List<TransferDto> movements;
}
