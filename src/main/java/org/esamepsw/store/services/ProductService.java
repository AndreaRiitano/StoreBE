package org.esamepsw.store.services;



import org.esamepsw.store.entities.Product;
import org.esamepsw.store.repositories.ProductRepository;
import org.esamepsw.store.utilities.exceptions.product.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) throws ProductNotFoundException {

        if(productRepository.findById(id).isPresent()) {
            return productRepository.findById(id).get();
        }else  {
            throw new ProductNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<Product> findByName(String name) throws ProductNotFoundException {
        if(productRepository.existsProductsByName(name)) {
            return productRepository.findProductsByName(name);
        }else   {
            throw new ProductNotFoundException();
        }
    }



}
