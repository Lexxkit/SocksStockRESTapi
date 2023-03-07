package com.lexxkit.socksstockaut.repository;

import com.lexxkit.socksstockaut.entity.SocksPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocksStockRepository extends JpaRepository<SocksPair, Long> {
}
