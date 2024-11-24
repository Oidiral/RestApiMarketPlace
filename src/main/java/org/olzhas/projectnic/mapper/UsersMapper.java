package org.olzhas.projectnic.mapper;

import org.mapstruct.*;
import org.olzhas.projectnic.dto.UsersDto;
import org.olzhas.projectnic.entity.User;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsersMapper {
    User toEntity(UsersDto usersDto);

    UsersDto toDto(User users);

    List<UsersDto> toDtos(List<User> users);

    List<User> toEntities(List<UsersDto> usersDtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(UsersDto usersDto, @MappingTarget User users);


}