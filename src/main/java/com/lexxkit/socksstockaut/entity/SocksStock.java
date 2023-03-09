package com.lexxkit.socksstockaut.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "socks_stocks")
public class SocksStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cottonPart;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;
}
