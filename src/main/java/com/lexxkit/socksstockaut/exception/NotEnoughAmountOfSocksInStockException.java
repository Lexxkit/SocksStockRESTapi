package com.lexxkit.socksstockaut.exception;

import com.lexxkit.socksstockaut.constant.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotEnoughAmountOfSocksInStockException extends RuntimeException {
    public NotEnoughAmountOfSocksInStockException() {
        super(AppConstants.NOT_ENOUGH_AMOUNT);
    }
}
