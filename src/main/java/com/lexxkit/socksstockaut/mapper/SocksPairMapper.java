package com.lexxkit.socksstockaut.mapper;

import com.lexxkit.socksstockaut.dto.SocksPairDto;
import com.lexxkit.socksstockaut.entity.SocksPair;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SocksPairMapper {
    SocksPairDto toDto(SocksPair socksPair);
    SocksPair toEntity(SocksPairDto socksPairDto);
}
