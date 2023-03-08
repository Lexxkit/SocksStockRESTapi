package com.lexxkit.socksstockaut.exception;

import com.lexxkit.socksstockaut.constant.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NoSuchSocksInStock extends RuntimeException {
    public NoSuchSocksInStock() {
        super(AppConstants.NO_SUCH_SOCKS_COMB);
    }
}
