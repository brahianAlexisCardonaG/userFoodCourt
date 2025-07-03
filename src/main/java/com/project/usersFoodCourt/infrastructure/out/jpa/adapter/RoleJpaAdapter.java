package com.project.usersFoodCourt.infrastructure.out.jpa.adapter;

import com.project.usersFoodCourt.domain.model.RoleModel;
import com.project.usersFoodCourt.domain.spi.IRolePersistencePort;
import com.project.usersFoodCourt.infrastructure.out.jpa.entity.RoleEntity;
import com.project.usersFoodCourt.infrastructure.out.jpa.mapper.IRoleEntityMapper;
import com.project.usersFoodCourt.infrastructure.out.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;

    @Override
    public Optional<RoleModel> findByRoleId(Long roleId) {
        Optional<RoleEntity> roleEntity = roleRepository.findById(roleId);
        return roleEntity.map(roleEntityMapper::toRoleModel);
    }
}
