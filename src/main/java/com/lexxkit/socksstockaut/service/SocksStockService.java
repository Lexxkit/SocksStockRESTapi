package com.lexxkit.socksstockaut.service;

import com.lexxkit.socksstockaut.constant.Operation;
import com.lexxkit.socksstockaut.dto.SocksStockDto;

public interface SocksStockService {
    int getQuantityOfSocksBy(String color, Operation operation, Integer cottonPart);

    void addSocks(SocksStockDto socksStockDto);

    void removeSocks(SocksStockDto socksStockDto);
}
