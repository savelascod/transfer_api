package yellowpepper.challenge.transfers.service.contract;


import org.springframework.security.core.userdetails.User;
import yellowpepper.challenge.transfers.persistence.entity.UserEntity;

import java.util.Optional;

public interface IUserService {
    Optional<UserEntity> getCurrentUserInSession(User user);

    Optional<UserEntity> findUserById(Integer userId);
}
