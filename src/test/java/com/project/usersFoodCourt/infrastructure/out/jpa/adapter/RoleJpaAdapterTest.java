package com.project.usersFoodCourt.infrastructure.out.jpa.adapter;

import com.project.usersFoodCourt.domain.model.RoleModel;
import com.project.usersFoodCourt.infrastructure.out.jpa.entity.RoleEntity;
import com.project.usersFoodCourt.infrastructure.out.jpa.mapper.IRoleEntityMapper;
import com.project.usersFoodCourt.infrastructure.out.jpa.repository.IRoleRepository;
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
class RoleJpaAdapterTest {

    @Mock
    private IRoleRepository roleRepository;
    @Mock
    private IRoleEntityMapper roleEntityMapper;

    @InjectMocks
    private RoleJpaAdapter roleJpaAdapter;

    private RoleModel roleModel;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        roleModel = new RoleModel();
        roleModel.setId(1L);
        roleModel.setName("USER");
        
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("USER");
    }

    @Test
    void findByRoleId_ShouldReturnRole_WhenRoleExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));
        when(roleEntityMapper.toRoleModel(roleEntity)).thenReturn(roleModel);

        Optional<RoleModel> result = roleJpaAdapter.findByRoleId(1L);

        assertTrue(result.isPresent());
        assertEquals(roleModel, result.get());
    }

    @Test
    void findByRoleId_ShouldReturnEmpty_WhenRoleNotExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RoleModel> result = roleJpaAdapter.findByRoleId(1L);

        assertFalse(result.isPresent());
    }
}