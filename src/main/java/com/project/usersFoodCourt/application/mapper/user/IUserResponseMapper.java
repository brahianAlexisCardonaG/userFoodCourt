package com.project.usersFoodCourt.application.mapper.user;

import com.project.usersFoodCourt.application.dto.response.UserRoleResponseDto;
import com.project.usersFoodCourt.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {
    UserRoleResponseDto toUserRoleResponseDto(UserModel userModel);
}
