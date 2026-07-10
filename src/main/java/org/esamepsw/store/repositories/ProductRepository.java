package org.esamepsw.store.repositories;


import org.esamepsw.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findProductsByName(String name);
    List<Product> findProductsByCategory(String category);
    Product findByCode(String code);
    boolean existsCategory(String category);
    boolean existsCode(String code);
    boolean existsProductByCode(String code);
    boolean existsProductById(long id);
    boolean existsProductsByName(String name);
}
