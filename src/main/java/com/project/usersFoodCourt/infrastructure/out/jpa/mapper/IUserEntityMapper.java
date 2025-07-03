package com.project.usersFoodCourt.infrastructure.out.jpa.mapper;

import com.project.usersFoodCourt.domain.model.UserModel;
import com.project.usersFoodCourt.infrastructure.out.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IUserEntityMapper {
    UserModel toUserModel(UserEntity userEntity);
    UserEntity toUserEntity(UserModel userModel);
}
