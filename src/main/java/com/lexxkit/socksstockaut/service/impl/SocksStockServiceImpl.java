package com.lexxkit.socksstockaut.service.impl;

import com.lexxkit.socksstockaut.constant.StockOperation;
import com.lexxkit.socksstockaut.dto.SocksStockDto;
import com.lexxkit.socksstockaut.entity.Color;
import com.lexxkit.socksstockaut.entity.SocksStock;
import com.lexxkit.socksstockaut.exception.NoSuchColorException;
import com.lexxkit.socksstockaut.exception.NoSuchSocksInStockException;
import com.lexxkit.socksstockaut.exception.NotEnoughAmountOfSocksInStockException;
import com.lexxkit.socksstockaut.mapper.SocksPairMapper;
import com.lexxkit.socksstockaut.repository.ColorRepository;
import com.lexxkit.socksstockaut.repository.SocksStockRepository;
import com.lexxkit.socksstockaut.service.SocksStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.lexxkit.socksstockaut.constant.AppConstants.ZERO;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocksStockServiceImpl implements SocksStockService {

    private final SocksStockRepository socksStockRepository;
    private final ColorRepository colorRepository;
    private final SocksPairMapper socksPairMapper;

    /**
     * Get number of socks available in stock based on color,
     * cotton part and comparison operators.
     * @param color name of color
     * @param stockOperation comparison operator from {@link StockOperation}
     * @param cottonPart desired amount of cotton
     * @return total number of socks matching the desired params OR zero if nothing matches
     */
    @Override
    public int getQuantityOfSocksBy(String color, StockOperation stockOperation, Integer cottonPart) {
        Collection<SocksStock> foundSocks = new ArrayList<>();
        switch (stockOperation) {
            case equal -> {
                Optional<SocksStock> socksStock = socksStockRepository.findByColorNameAndCottonPart(color, cottonPart);
                return socksStock.isPresent() ? socksStock.get().getQuantity() : ZERO;
            }
            case moreThan ->
                    foundSocks = socksStockRepository.findByColorNameAndCottonPartAfter(color, cottonPart);
            case lessThan ->
                    foundSocks = socksStockRepository.findByColorNameAndCottonPartBefore(color, cottonPart);
        }
        return foundSocks.stream().map(SocksStock::getQuantity).reduce(ZERO, Integer::sum);
    }

    /**
     * Add socks to DB. If there is NO socks with certain color and cotton part, then new entry will be saved.
     * If DB contains entry with the same color and cotton part, then its amount will be increased by quantity.
     * @param socksStockDto {@link SocksStockDto} instance from user's request.
     */
    @Transactional
    @Override
    public void addSocks(SocksStockDto socksStockDto) {
        //Get color by its name from DB or create new entry
        Color currentColor = colorRepository.findByName(socksStockDto.getColor())
                .orElseGet(() -> createColor(socksStockDto));
        //Find socks in DB by color AND cottonPart
        Optional<SocksStock> stockOptional = socksStockRepository
                .findByColorIdAndCottonPart(currentColor.getId(), socksStockDto.getCottonPart());
        SocksStock newSocksStock;
        if (stockOptional.isPresent()) {
            //If found - update quantity
            newSocksStock = stockOptional.get();
            newSocksStock.setQuantity(newSocksStock.getQuantity() + socksStockDto.getQuantity());
        } else {
            //If NOT found - create new instance with mapper and set color
            newSocksStock = socksPairMapper.toEntity(socksStockDto);
            newSocksStock.setColor(currentColor);
        }

        socksStockRepository.save(newSocksStock);
    }

    /**
     * Remove desired quantity of socks from DB.
     * @param socksStockDto {@link SocksStockDto} instance from user's request.
     * @throws NoSuchColorException if color name was NOT found in DB
     * @throws NoSuchSocksInStockException if combination of color and cotton part was NOT found in DB
     * @throws NotEnoughAmountOfSocksInStockException if quantity to remove exceeds available amount of desired socks
     */
    @Override
    public void removeSocks(SocksStockDto socksStockDto) {
        Color currentColor = colorRepository.findByName(socksStockDto.getColor())
                .orElseThrow(NoSuchColorException::new);
        SocksStock socksStock = socksStockRepository.findByColorIdAndCottonPart(currentColor.getId(), socksStockDto.getCottonPart())
                .orElseThrow(NoSuchSocksInStockException::new);
        if (socksStock.getQuantity() < socksStockDto.getQuantity()) {
            throw new NotEnoughAmountOfSocksInStockException();
        }
        socksStock.setQuantity(socksStock.getQuantity() - socksStockDto.getQuantity());
        socksStockRepository.save(socksStock);
    }

    /**
     * Creates new {@link Color} instance and saves it in DB.
     * @param socksStockDto {@link SocksStockDto} instance
     * @return created instance of {@link Color}
     */
    private Color createColor(SocksStockDto socksStockDto) {
        Color currentColor = new Color();
        currentColor.setName(socksStockDto.getColor());
        return colorRepository.save(currentColor);
    }
}
