package ru.sber.app.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.app.domain.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
