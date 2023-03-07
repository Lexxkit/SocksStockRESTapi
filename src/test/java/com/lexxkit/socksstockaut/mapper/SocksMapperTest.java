package com.lexxkit.socksstockaut.mapper;

import com.lexxkit.socksstockaut.dto.SocksPairDto;
import com.lexxkit.socksstockaut.entity.SocksPair;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class SocksMapperTest {
    private final SocksPairMapper mapper = Mappers.getMapper(SocksPairMapper.class);

    @Test
    public void shouldMapSocksPairToDto() {
        //given
        SocksPair socksPair = new SocksPair();
        socksPair.setId(1L);
        socksPair.setColor("red");
        socksPair.setQuantity(2);
        socksPair.setCottonPart(85);

        //when
        SocksPairDto out = mapper.toDto(socksPair);

        //then
        assertThat(out).isNotNull();
        assertThat(out.getColor()).isEqualTo(socksPair.getColor());
        assertThat(out.getQuantity()).isEqualTo(socksPair.getQuantity());
        assertThat(out.getCottonPart()).isEqualTo(socksPair.getCottonPart());
    }

    @Test
    public void shouldMapSocksPairDtoToEntity() {
        //given
        SocksPairDto socksPairDto = new SocksPairDto();
        socksPairDto.setColor("green");
        socksPairDto.setQuantity(1);
        socksPairDto.setCottonPart(42);

        //when
        SocksPair out = mapper.toEntity(socksPairDto);

        //then
        assertThat(out).isNotNull();
        assertThat(out.getId()).isNull();
        assertThat(out.getColor()).isEqualTo(socksPairDto.getColor());
        assertThat(out.getQuantity()).isEqualTo(socksPairDto.getQuantity());
        assertThat(out.getCottonPart()).isEqualTo(socksPairDto.getCottonPart());
    }
}
