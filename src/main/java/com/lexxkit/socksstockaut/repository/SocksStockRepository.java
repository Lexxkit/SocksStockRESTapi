package com.lexxkit.socksstockaut.repository;

import com.lexxkit.socksstockaut.entity.SocksStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SocksStockRepository extends JpaRepository<SocksStock, Long> {
    Optional<SocksStock> findByColorIdAndCottonPart(long colorId, int cottonPart);

    Optional<SocksStock> findByColorNameAndCottonPart(String colorName, int cottonPart);
    Collection<SocksStock> findByColorNameAndCottonPartAfter(String colorName, int cottonPart);
    Collection<SocksStock> findByColorNameAndCottonPartBefore(String colorName, int cottonPart);
}
