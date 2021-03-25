package yellowpepper.challenge.transfers.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import yellowpepper.challenge.transfers.persistence.entity.UserEntity;
import yellowpepper.challenge.transfers.persistence.repository.IUserRepository;
import yellowpepper.challenge.transfers.service.contract.IUserService;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> getCurrentUserInSession(User user) {
        return userRepository.findByUsername(user.getUsername());
    }

    @Override
    public Optional<UserEntity> findUserById(Integer userId) {
        return userRepository.findById(userId);
    }
}
