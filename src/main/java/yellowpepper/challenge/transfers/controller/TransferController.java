package yellowpepper.challenge.transfers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import yellowpepper.challenge.transfers.delegate.TransferDelegate;
import yellowpepper.challenge.transfers.dto.request.GetTransferFilterDto;
import yellowpepper.challenge.transfers.dto.request.TransferRequestDto;
import yellowpepper.challenge.transfers.dto.response.GetTransfersResponseDto;
import yellowpepper.challenge.transfers.dto.response.TransferResponseDto;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferDelegate transferDelegate;

    @Autowired
    public TransferController(TransferDelegate transferDelegate) {
        this.transferDelegate = transferDelegate;
    }

    @PostMapping("/movements")
    public ResponseEntity<GetTransfersResponseDto> getTransfers(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestBody(required = false) GetTransferFilterDto transferFilterDto,
            @AuthenticationPrincipal User currentUser) {
        if (transferFilterDto == null) transferFilterDto = new GetTransferFilterDto();
        return ResponseEntity.ok().body(GetTransfersResponseDto.builder()
                .movements(transferDelegate.getAllUserMovements(page, size, transferFilterDto, currentUser)).build());
    }

    @PostMapping
    public ResponseEntity<TransferResponseDto> performTransfer(@RequestBody TransferRequestDto transferRequestDto) {
        return ResponseEntity.ok().body(TransferResponseDto.builder()
                .violations(transferDelegate.performTransfer(transferRequestDto).
                        stream().map(businessException -> businessException.getMessage())
                        .collect(Collectors.toList())).build());
    }

}
