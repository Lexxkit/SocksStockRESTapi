package com.lexxkit.socksstockaut.controller;

import com.lexxkit.socksstockaut.constant.Operation;
import com.lexxkit.socksstockaut.dto.SocksStockDto;
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
    public ResponseEntity<Integer> getQuantityOfSocksBy(@RequestParam String color,
                                                        @RequestParam Operation operation,
                                                        @RequestParam Integer cottonPart) {
        log.info("Was invoked 'getQualityOfSocksBy' method from {}", SocksStockController.class.getSimpleName());
        return ResponseEntity.ok(socksStockService.getQuantityOfSocksBy(color, operation, cottonPart));
    }

    @PostMapping(path = "/income")
    public ResponseEntity<SocksStockDto> addSocksToStock(@RequestBody SocksStockDto socksStockDto) {
        log.info("Was invoked 'addSocksToStock' method from {}", SocksStockController.class.getSimpleName());
        socksStockService.addSocks(socksStockDto);
        return ResponseEntity.ok(socksStockDto);
    }

    @PostMapping(path = "/outcome")
    public ResponseEntity<SocksStockDto> takeSocksFromStock(@RequestBody SocksStockDto socksStockDto) {
        log.info("Was invoked 'takeSocksFromStock' method from {}", SocksStockController.class.getSimpleName());
        socksStockService.removeSocks(socksStockDto);
        return ResponseEntity.ok(socksStockDto);
    }
}
