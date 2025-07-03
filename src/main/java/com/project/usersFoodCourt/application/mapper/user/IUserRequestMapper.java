package com.project.usersFoodCourt.application.mapper.user;

import com.project.usersFoodCourt.application.dto.request.UserAuthenticateRequestDto;
import com.project.usersFoodCourt.application.dto.request.UserRegisterRequestDto;
import com.project.usersFoodCourt.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
    @Mapping(source = "roleId", target = "role.id")
    UserModel toUserRegisterModel(UserRegisterRequestDto userRegisterRequestDto);
    UserModel toUserAuthenticateModel(UserAuthenticateRequestDto userAuthenticateRequestDto);
}
