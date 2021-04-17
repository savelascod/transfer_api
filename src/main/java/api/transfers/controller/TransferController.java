package api.transfers.controller;

import api.transfers.dto.request.GetTransferFilterDto;
import api.transfers.dto.response.GetTransfersResponseDto;
import api.transfers.dto.response.TransferResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import api.transfers.delegate.TransferDelegate;
import api.transfers.dto.request.TransferRequestDto;

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
