package com.project.usersFoodCourt.infrastructure.out.jpa.adapter;

import com.project.usersFoodCourt.domain.model.UserModel;
import com.project.usersFoodCourt.domain.spi.IUserPersistencePort;
import com.project.usersFoodCourt.infrastructure.out.jpa.entity.UserEntity;
import com.project.usersFoodCourt.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.project.usersFoodCourt.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public void save(UserModel user) {
        UserEntity userEntity = userEntityMapper.toUserEntity(user);
        UserEntity userSaved = userRepository.save(userEntity);
        userEntityMapper.toUserModel(userSaved);
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        return userEntity.map(userEntityMapper::toUserModel);
    }

    @Override
    public Optional<UserModel> findById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.map(userEntityMapper::toUserModel);
    }

    @Override
    public Optional<UserModel> findByDocument(String documentNumber) {
        Optional<UserEntity> userEntity = userRepository.findByDocumentNumber(documentNumber);
        return userEntity.map(userEntityMapper::toUserModel);
    }
}
