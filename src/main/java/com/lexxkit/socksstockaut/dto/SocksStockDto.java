package com.lexxkit.socksstockaut.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class SocksStockDto {
    @NotBlank
    private String color;
    @Min(0)
    @Max(100)
    private int cottonPart;
    @Min(1)
    private int quantity;
}
