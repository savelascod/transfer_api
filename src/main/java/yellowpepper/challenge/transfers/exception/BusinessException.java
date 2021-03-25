package yellowpepper.challenge.transfers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends Exception {
    private String message;
}
