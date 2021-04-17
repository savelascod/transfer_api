package api.transfers.service.impl;


import api.transfers.persistence.repository.IUserRepository;
import api.transfers.service.contract.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import api.transfers.persistence.entity.UserEntity;

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
