package com.lexxkit.socksstockaut.service.impl;

import com.lexxkit.socksstockaut.entity.SocksPair;
import com.lexxkit.socksstockaut.repository.SocksStockRepository;
import com.lexxkit.socksstockaut.service.SocksStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocksStockServiceImpl implements SocksStockService {

    private final SocksStockRepository socksStockRepository;
    @Override
    public int getQuantityOfSocksBy() {
        List<SocksPair> all = socksStockRepository.findAll();
        return all.stream().map(SocksPair::getQuantity).reduce(0, Integer::sum);
    }
}
