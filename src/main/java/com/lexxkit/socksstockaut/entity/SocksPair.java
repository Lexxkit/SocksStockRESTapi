package com.lexxkit.socksstockaut.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "socks_pairs")
public class SocksPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String color;
    private Integer cottonPart;
    private Integer quantity;
}
