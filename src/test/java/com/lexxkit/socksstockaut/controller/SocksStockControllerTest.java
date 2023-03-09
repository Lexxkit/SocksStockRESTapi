package com.lexxkit.socksstockaut.controller;

import com.lexxkit.socksstockaut.dto.SocksStockDto;
import com.lexxkit.socksstockaut.entity.Color;
import com.lexxkit.socksstockaut.entity.SocksStock;
import com.lexxkit.socksstockaut.mapper.SocksPairMapperImpl;
import com.lexxkit.socksstockaut.repository.ColorRepository;
import com.lexxkit.socksstockaut.repository.SocksStockRepository;
import com.lexxkit.socksstockaut.service.impl.SocksStockServiceImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SocksStockController.class)
public class SocksStockControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ColorRepository colorRepository;
    @MockBean
    private SocksStockRepository socksStockRepository;
    @SpyBean
    private SocksStockServiceImpl socksStockService;
    @SpyBean
    private SocksPairMapperImpl socksPairMapper;
    @InjectMocks
    private SocksStockController socksStockController;

    private SocksStock testSocks;
    private Color testColor;
    private SocksStockDto testDto;

    @BeforeEach
    void init() {
        testColor = new Color();
        testColor.setId(1L);
        testColor.setName("red");

        testSocks = new SocksStock();
        testSocks.setId(null);
        testSocks.setCottonPart(50);
        testSocks.setQuantity(1);
        testSocks.setColor(testColor);

        testDto = new SocksStockDto();
        testDto.setColor(testColor.getName());
        testDto.setQuantity(testSocks.getQuantity());
        testDto.setCottonPart(testSocks.getCottonPart());
    }

    @Test
    void shouldReturnInt_whenGetQuantityOfSocksBy() throws Exception {
        when(socksStockRepository.findByColorNameAndCottonPart(anyString(), anyInt())).thenReturn(Optional.empty());
        when(socksStockRepository.findByColorNameAndCottonPartAfter(anyString(), anyInt())).thenReturn(Collections.emptyList());
        when(socksStockRepository.findByColorNameAndCottonPartBefore(anyString(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/socks")
                .queryParam("color", "red")
                .queryParam("operation", "equal")
                .queryParam("cottonPart", "50"))
                .andExpect(status().isOk())
                .andExpect(content().json("0"));

        mockMvc.perform(get("/socks")
                        .queryParam("color", "red")
                        .queryParam("operation", "moreThan")
                        .queryParam("cottonPart", "50"))
                .andExpect(status().isOk())
                .andExpect(content().json("0"));

        mockMvc.perform(get("/socks")
                        .queryParam("color", "red")
                        .queryParam("operation", "lessThan")
                        .queryParam("cottonPart", "50"))
                .andExpect(status().isOk())
                .andExpect(content().json("0"));
    }

    @Test
    void shouldPass_whenAddSocks() throws Exception {
        JSONObject testSocksObject = new JSONObject();
        testSocksObject.put("color", testDto.getColor());
        testSocksObject.put("cottonPart", testDto.getCottonPart());
        testSocksObject.put("quantity", testDto.getQuantity());

        when(colorRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(colorRepository.save(any(Color.class))).thenReturn(testColor);
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(post("/socks/income")
                        .content(testSocksObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFail_whenAddSocksNotValid() throws Exception {
        JSONObject testSocksObject = new JSONObject();
        testSocksObject.put("color", testDto.getColor());
        testSocksObject.put("cottonPart", testDto.getCottonPart());
        testSocksObject.put("quantity", testDto.getQuantity() * 0);

        when(colorRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(colorRepository.save(any(Color.class))).thenReturn(testColor);
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(post("/socks/income")
                        .content(testSocksObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldPass_whenRemoveSocks() throws Exception {
        JSONObject testSocksObject = new JSONObject();
        testSocksObject.put("color", testDto.getColor());
        testSocksObject.put("cottonPart", testDto.getCottonPart());
        testSocksObject.put("quantity", testDto.getQuantity());

        when(colorRepository.findByName(anyString())).thenReturn(Optional.of(testColor));
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.of(testSocks));

        mockMvc.perform(post("/socks/outcome")
                        .content(testSocksObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFail_whenRemoveSocksMoreThanHave() throws Exception {
        JSONObject testSocksObject = new JSONObject();
        testSocksObject.put("color", testDto.getColor());
        testSocksObject.put("cottonPart", testDto.getCottonPart());
        testSocksObject.put("quantity", testDto.getQuantity() * 2);

        when(colorRepository.findByName(anyString())).thenReturn(Optional.of(testColor));
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.of(testSocks));

        mockMvc.perform(post("/socks/outcome")
                        .content(testSocksObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldFail_whenRemoveSocksNotValid() throws Exception {
        JSONObject testSocksObject = new JSONObject();
        testSocksObject.put("color", testDto.getColor());
        testSocksObject.put("cottonPart", testDto.getCottonPart() * 1000);
        testSocksObject.put("quantity", testDto.getQuantity());

        when(colorRepository.findByName(anyString())).thenReturn(Optional.of(testColor));
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.of(testSocks));

        mockMvc.perform(post("/socks/outcome")
                        .content(testSocksObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
