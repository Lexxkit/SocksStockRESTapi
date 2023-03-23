package com.lexxkit.socksstockaut.mapper;

import com.lexxkit.socksstockaut.dto.SocksStockDto;
import com.lexxkit.socksstockaut.entity.Color;
import com.lexxkit.socksstockaut.entity.SocksStock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class SocksMapperTest {
    private final SocksPairMapper mapper = Mappers.getMapper(SocksPairMapper.class);

    @Test
    void shouldMapSocksPairToDto() {
        //given
        Color color = new Color();
        color.setId(1L);
        color.setName("red");
        SocksStock socksStock = new SocksStock();
        socksStock.setId(1L);
        socksStock.setColor(color);
        socksStock.setQuantity(2);
        socksStock.setCottonPart(85);

        //when
        SocksStockDto out = mapper.toDto(socksStock);

        //then
        assertThat(out).isNotNull();
        assertThat(out.getColor()).isEqualTo(socksStock.getColor().getName());
        assertThat(out.getQuantity()).isEqualTo(socksStock.getQuantity());
        assertThat(out.getCottonPart()).isEqualTo(socksStock.getCottonPart());
    }

    @Test
    void shouldMapSocksPairDtoToEntity() {
        //given
        SocksStockDto socksStockDto = new SocksStockDto();
        socksStockDto.setColor("green");
        socksStockDto.setQuantity(1);
        socksStockDto.setCottonPart(42);

        //when
        SocksStock out = mapper.toEntity(socksStockDto);

        //then
        assertThat(out).isNotNull();
        assertThat(out.getId()).isNull();
        assertThat(out.getColor().getName()).isEqualTo(socksStockDto.getColor());
        assertThat(out.getQuantity()).isEqualTo(socksStockDto.getQuantity());
        assertThat(out.getCottonPart()).isEqualTo(socksStockDto.getCottonPart());
    }
}
