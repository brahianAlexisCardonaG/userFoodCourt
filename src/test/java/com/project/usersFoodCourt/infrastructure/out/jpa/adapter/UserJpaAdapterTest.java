package com.project.usersFoodCourt.infrastructure.out.jpa.adapter;

import com.project.usersFoodCourt.domain.model.UserModel;
import com.project.usersFoodCourt.infrastructure.out.jpa.entity.UserEntity;
import com.project.usersFoodCourt.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.project.usersFoodCourt.infrastructure.out.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJpaAdapterTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IUserEntityMapper userEntityMapper;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    private UserModel userModel;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userModel = new UserModel();
        userModel.setEmail("test@test.com");
        
        userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
    }

    @Test
    void save_ShouldSaveUser() {
        when(userEntityMapper.toUserEntity(userModel)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(userModel);

        userJpaAdapter.save(userModel);

        verify(userRepository).save(userEntity);
        verify(userEntityMapper).toUserEntity(userModel);
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(userModel);

        Optional<UserModel> result = userJpaAdapter.findByEmail("test@test.com");

        assertTrue(result.isPresent());
        assertEquals(userModel, result.get());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenUserNotExists() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        Optional<UserModel> result = userJpaAdapter.findByEmail("test@test.com");

        assertFalse(result.isPresent());
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(userModel);

        Optional<UserModel> result = userJpaAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(userModel, result.get());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenUserNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserModel> result = userJpaAdapter.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void findByDocument_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByDocumentNumber("12345678")).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUserModel(userEntity)).thenReturn(userModel);

        Optional<UserModel> result = userJpaAdapter.findByDocument("12345678");

        assertTrue(result.isPresent());
        assertEquals(userModel, result.get());
    }

    @Test
    void findByDocument_ShouldReturnEmpty_WhenUserNotExists() {
        when(userRepository.findByDocumentNumber("12345678")).thenReturn(Optional.empty());

        Optional<UserModel> result = userJpaAdapter.findByDocument("12345678");

        assertFalse(result.isPresent());
    }
}