package com.project.usersFoodCourt.domain.spi;

import com.project.usersFoodCourt.domain.model.UserModel;

import java.util.Optional;

public interface IUserPersistencePort {
    void save(UserModel user);
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findById(Long id);
    Optional<UserModel> findByDocument(String documentNumber);
}
