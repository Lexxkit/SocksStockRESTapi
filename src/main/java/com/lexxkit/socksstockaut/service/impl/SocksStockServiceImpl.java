package com.lexxkit.socksstockaut.service.impl;

import com.lexxkit.socksstockaut.constant.Operation;
import com.lexxkit.socksstockaut.dto.SocksStockDto;
import com.lexxkit.socksstockaut.entity.Color;
import com.lexxkit.socksstockaut.entity.SocksStock;
import com.lexxkit.socksstockaut.exception.NoSuchColorException;
import com.lexxkit.socksstockaut.exception.NoSuchSocksInStock;
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
    @Override
    public int getQuantityOfSocksBy(String color, Operation operation, Integer cottonPart) {
        Collection<SocksStock> foundSocks = new ArrayList<>();
        switch (operation) {
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
            //If NOT found - create new entry with mapper and set color
            newSocksStock = socksPairMapper.toEntity(socksStockDto);
            newSocksStock.setColor(currentColor);
        }

        socksStockRepository.save(newSocksStock);
    }

    @Override
    public void removeSocks(SocksStockDto socksStockDto) {
        Color currentColor = colorRepository.findByName(socksStockDto.getColor())
                .orElseThrow(NoSuchColorException::new);
        SocksStock socksStock = socksStockRepository.findByColorIdAndCottonPart(currentColor.getId(), socksStockDto.getCottonPart())
                .orElseThrow(NoSuchSocksInStock::new);
        if (socksStock.getQuantity() < socksStockDto.getQuantity()) {
            throw new NotEnoughAmountOfSocksInStockException();
        }
        socksStock.setQuantity(socksStock.getQuantity() - socksStockDto.getQuantity());
        socksStockRepository.save(socksStock);
    }

    private Color createColor(SocksStockDto socksStockDto) {
        Color currentColor = new Color();
        currentColor.setName(socksStockDto.getColor());
        return colorRepository.save(currentColor);
    }
}
