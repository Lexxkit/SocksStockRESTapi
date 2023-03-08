package com.lexxkit.socksstockaut.service;

import com.lexxkit.socksstockaut.constant.StockOperation;
import com.lexxkit.socksstockaut.dto.SocksStockDto;

public interface SocksStockService {
    int getQuantityOfSocksBy(String color, StockOperation stockOperation, Integer cottonPart);

    void addSocks(SocksStockDto socksStockDto);

    void removeSocks(SocksStockDto socksStockDto);
}
