package api.transfers.dto.response;

import api.transfers.dto.domain.TransferDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTransfersResponseDto {
    List<TransferDto> movements;
}
