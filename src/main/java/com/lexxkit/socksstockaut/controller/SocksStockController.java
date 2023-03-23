package com.lexxkit.socksstockaut.controller;

import com.lexxkit.socksstockaut.constant.StockOperation;
import com.lexxkit.socksstockaut.dto.SocksStockDto;
import com.lexxkit.socksstockaut.service.SocksStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/socks")
public class SocksStockController {

    private final SocksStockService socksStockService;

    @Operation(summary = "getQuantityOfSocksBy(params...)",
            responses = {
                    @ApiResponse(responseCode = "200",
                                content = @Content(schema = @Schema(implementation = Integer.class))),
                    @ApiResponse(responseCode = "400", description = "Missing or incompatible params.", content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "500", description = "Server side error occurs.", content = @Content(schema = @Schema()))
            })
    @GetMapping
    public ResponseEntity<Integer> getQuantityOfSocksBy(@RequestParam String color,
                                                        @RequestParam StockOperation operation,
                                                        @RequestParam Integer cottonPart) {
        log.info("Was invoked 'getQualityOfSocksBy' method from {}", SocksStockController.class.getSimpleName());
        return ResponseEntity.ok(socksStockService.getQuantityOfSocksBy(color, operation, cottonPart));
    }

    @Operation(summary = "addSocksToStock",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock was refiled successfully."),
                    @ApiResponse(responseCode = "400", description = "Missing or incompatible params."),
                    @ApiResponse(responseCode = "500", description = "Server side error occurs.")
            })
    @PostMapping(path = "/income")
    public ResponseEntity<Void> addSocksToStock(@Valid @RequestBody SocksStockDto socksStockDto) {
        log.info("Was invoked 'addSocksToStock' method from {}", SocksStockController.class.getSimpleName());
        socksStockService.addSocks(socksStockDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "removeSocksFromStock",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Socks was removed successfully."),
                    @ApiResponse(responseCode = "400", description = "Missing or incompatible params."),
                    @ApiResponse(responseCode = "500", description = "Server side error occurs.")
            })
    @PostMapping(path = "/outcome")
    public ResponseEntity<Void> takeSocksFromStock(@Valid @RequestBody SocksStockDto socksStockDto) {
        log.info("Was invoked 'takeSocksFromStock' method from {}", SocksStockController.class.getSimpleName());
        socksStockService.removeSocks(socksStockDto);
        return ResponseEntity.ok().build();
    }
}
