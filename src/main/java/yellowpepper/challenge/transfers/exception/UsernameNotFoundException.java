package yellowpepper.challenge.transfers.exception;

import lombok.Getter;

@Getter
public class UsernameNotFoundException extends Exception {
    private String message;

    public UsernameNotFoundException(String username) {
        this.message = String.format("User not found with username: %s", username);
    }
}
