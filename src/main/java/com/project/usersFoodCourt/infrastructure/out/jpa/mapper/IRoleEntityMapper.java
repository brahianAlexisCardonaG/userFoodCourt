package com.project.usersFoodCourt.infrastructure.out.jpa.mapper;

import com.project.usersFoodCourt.domain.model.RoleModel;
import com.project.usersFoodCourt.infrastructure.out.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRoleEntityMapper {
    RoleEntity toRoleEntity(RoleModel roleModel);
    RoleModel toRoleModel(RoleEntity roleEntity);
}
