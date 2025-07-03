package com.project.usersFoodCourt.domain.spi;

import com.project.usersFoodCourt.domain.model.RoleModel;

import java.util.Optional;

public interface IRolePersistencePort {
   Optional<RoleModel> findByRoleId(Long roleId);
}
