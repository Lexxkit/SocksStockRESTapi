package com.lexxkit.socksstockaut.controller;

import com.lexxkit.socksstockaut.dto.SocksPairDto;
import com.lexxkit.socksstockaut.service.SocksStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/socks")
public class SocksStockController {

    private final SocksStockService socksStockService;

    @GetMapping
    public ResponseEntity<Integer> getQuantityOfSocksBy() {
        log.info("Was invoked 'getQualityOfSocksBy' method from {}", SocksStockController.class.getSimpleName());
        int quantity = socksStockService.getQuantityOfSocksBy();
        return ResponseEntity.ok(quantity);
    }

    @PostMapping(path = "/income")
    public ResponseEntity<SocksPairDto> addSocksToStock(@RequestBody SocksPairDto socksPairDto) {
        log.info("Was invoked 'addSocksToStock' method from {}", SocksStockController.class.getSimpleName());
        return ResponseEntity.ok(socksPairDto);
    }

    @PostMapping(path = "/outcome")
    public ResponseEntity<SocksPairDto> takeSocksFromStock(@RequestBody SocksPairDto socksPairDto) {
        log.info("Was invoked 'takeSocksFromStock' method from {}", SocksStockController.class.getSimpleName());
        return ResponseEntity.ok(socksPairDto);
    }
}
