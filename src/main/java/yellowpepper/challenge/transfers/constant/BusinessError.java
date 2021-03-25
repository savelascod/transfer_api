package yellowpepper.challenge.transfers.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BusinessError {
    USER_NOT_EXIST("The user with id number %s does not exist in database"),
    SENDER_ACCOUNT_NOT_EXIST("The sender account does not exist in database"),
    RECIPIENT_ACCOUNT_NOT_EXIST("The recipient account does not exist in database"),
    ACCOUNT_OWNERSHIP_ERROR("The account with number %s, does not belong to user %s"),
    CURRENCY_CONVERSION_ERROR("The currency is not supported, please try another");
    String message;
}
