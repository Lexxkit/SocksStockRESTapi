package com.lexxkit.socksstockaut.service;

import com.lexxkit.socksstockaut.constant.AppConstants;
import com.lexxkit.socksstockaut.constant.Operation;
import com.lexxkit.socksstockaut.dto.SocksStockDto;
import com.lexxkit.socksstockaut.entity.Color;
import com.lexxkit.socksstockaut.entity.SocksStock;
import com.lexxkit.socksstockaut.exception.NoSuchColorException;
import com.lexxkit.socksstockaut.exception.NoSuchSocksInStockException;
import com.lexxkit.socksstockaut.exception.NotEnoughAmountOfSocksInStockException;
import com.lexxkit.socksstockaut.mapper.SocksPairMapper;
import com.lexxkit.socksstockaut.mapper.SocksPairMapperImpl;
import com.lexxkit.socksstockaut.repository.ColorRepository;
import com.lexxkit.socksstockaut.repository.SocksStockRepository;
import com.lexxkit.socksstockaut.service.impl.SocksStockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SocksStockServiceImplTest {
    @Mock
    private ColorRepository colorRepository;
    @Mock
    private SocksStockRepository socksStockRepository;
    @Spy
    private SocksPairMapper socksPairMapper = new SocksPairMapperImpl();
    @InjectMocks
    private SocksStockServiceImpl out;

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
    void shouldReturnZeroForEachOperationType_whenGetQuantityOfSocksAndSocksNotFound() {
        when(socksStockRepository.findByColorNameAndCottonPart(anyString(), anyInt())).thenReturn(Optional.empty());
        when(socksStockRepository.findByColorNameAndCottonPartAfter(anyString(), anyInt())).thenReturn(Collections.emptyList());
        when(socksStockRepository.findByColorNameAndCottonPartBefore(anyString(), anyInt())).thenReturn(Collections.emptyList());

        int resultEqual = out.getQuantityOfSocksBy("red", Operation.equal, 0);
        int resultMoreThan = out.getQuantityOfSocksBy("red", Operation.moreThan, 0);
        int resultLessThan = out.getQuantityOfSocksBy("red", Operation.lessThan, 0);

        assertThat(resultEqual).isEqualTo(AppConstants.ZERO);
        assertThat(resultMoreThan).isEqualTo(AppConstants.ZERO);
        assertThat(resultLessThan).isEqualTo(AppConstants.ZERO);
    }

    @Test
    void shouldReturnIntegerForEachOperationType_whenGetQuantityOfSocksAndSocksFound() {
        when(socksStockRepository.findByColorNameAndCottonPart(anyString(), anyInt())).thenReturn(Optional.of(testSocks));
        when(socksStockRepository.findByColorNameAndCottonPartAfter(anyString(), anyInt())).thenReturn(List.of(testSocks));
        when(socksStockRepository.findByColorNameAndCottonPartBefore(anyString(), anyInt())).thenReturn(List.of(testSocks));

        int resultEqual = out.getQuantityOfSocksBy("red", Operation.equal, 0);
        int resultMoreThan = out.getQuantityOfSocksBy("red", Operation.moreThan, 0);
        int resultLessThan = out.getQuantityOfSocksBy("red", Operation.lessThan, 0);

        assertThat(resultEqual).isEqualTo(testSocks.getQuantity());
        assertThat(resultMoreThan).isEqualTo(testSocks.getQuantity());
        assertThat(resultLessThan).isEqualTo(testSocks.getQuantity());
    }

    @Test
    void shouldUseCreateColorAndSaveSocksOnce_whenAddSocksWithNewCombination() {
        when(colorRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(colorRepository.save(any(Color.class))).thenReturn(testColor);
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.empty());

        out.addSocks(testDto);

        verify(socksStockRepository, times(1)).save(testSocks);
    }

    @Test
    void shouldAddQuantityAndSaveSocksOnce_whenAddSocksWithPresentCombination() {
        when(colorRepository.findByName(anyString())).thenReturn(Optional.of(testColor));
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.of(testSocks));

        out.addSocks(testDto);

        verify(socksStockRepository, times(1)).save(testSocks);
    }

    @Test
    void shouldDecreaseQuantityAndSaveSocksOnce_whenRemoveSocksWithPresentCombination() {
        when(colorRepository.findByName(anyString())).thenReturn(Optional.of(testColor));
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.of(testSocks));

        assertThatNoException().isThrownBy(() -> out.removeSocks(testDto));
        verify(socksStockRepository, times(1)).save(testSocks);
    }

    @Test
    void shouldThrowNoSuchColorException_whenRemoveSocksWithWrongColor() {
        when(colorRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchColorException.class).describedAs(AppConstants.NO_SUCH_COLOR)
                .isThrownBy(() -> out.removeSocks(testDto));
    }

    @Test
    void shouldThrowNoSuchSocksInStockException_whenRemoveSocksWithWrongCombination() {
        when(colorRepository.findByName(anyString())).thenReturn(Optional.of(testColor));
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchSocksInStockException.class).describedAs(AppConstants.NO_SUCH_SOCKS_COMB)
                .isThrownBy(() -> out.removeSocks(testDto));
    }

    @Test
    void shouldThrowNotEnoughAmountOfSocksInStockException_whenRemoveSocksWithLargerQuantity() {
        when(colorRepository.findByName(anyString())).thenReturn(Optional.of(testColor));
        when(socksStockRepository.findByColorIdAndCottonPart(anyLong(), anyInt())).thenReturn(Optional.of(testSocks));

        //Set more quantity in testDto than in testSocks
        testDto.setQuantity(testSocks.getQuantity() + 1);
        assertThatExceptionOfType(NotEnoughAmountOfSocksInStockException.class).describedAs(AppConstants.NOT_ENOUGH_AMOUNT)
                .isThrownBy(() -> out.removeSocks(testDto));
    }
}
