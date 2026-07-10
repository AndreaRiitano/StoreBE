package org.esamepsw.store.services;



import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.esamepsw.store.entities.Product;
import org.esamepsw.store.repositories.ProductRepository;
import org.esamepsw.store.utilities.exceptions.product.ProductAlreadyExistsException;
import org.esamepsw.store.utilities.exceptions.product.ProductCategoryNotFound;
import org.esamepsw.store.utilities.exceptions.product.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @PersistenceContext
    private EntityManager entityManager;

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
    public Product findByCode(String code) {
        if(productRepository.existsProductByCode(code)) {
            return productRepository.findByCode(code);

        }else   {
            throw new ProductNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategory(String category) {
        if(productRepository.existsCategory(category)) {
            return productRepository.findProductsByCategory(category);
        }else   {
            throw new ProductCategoryNotFound();
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

    @Transactional(readOnly = false)
    public Product addProduct(Product product) throws ProductAlreadyExistsException {

        if(productRepository.existsProductByCode(product.getCode())) {
            throw new ProductAlreadyExistsException();
        }
        productRepository.save(product);
        return product;

    }

    @Transactional(readOnly = false)
    public Product updateProduct(Product product) throws ProductNotFoundException {

        if(! productRepository.existsProductById(product.getId()) || ! productRepository.existsProductByCode(product.getCode())) {

            throw new ProductNotFoundException();
        }

        Product originalProduct = productRepository.findById(product.getId()).get();
        entityManager.lock(originalProduct, LockModeType.OPTIMISTIC);

        if(product.getName()!=null) {
            product.setName(product.getName());
        }
        if(product.getDescription()!=null) {
            product.setDescription(product.getDescription());
        }
        if(product.getCode()!=null) {
            product.setCode(product.getCode());
        }
        if(product.getCategory()!=null) {
            product.setCategory(product.getCategory());
        }
        if(product.getPrice()>0.0){
            product.setPrice(product.getPrice());
        }
        if(product.getQuantity()>=0){
            product.setQuantity(product.getQuantity());
        }


        return originalProduct;
    }

    @Transactional(readOnly = false)
    public void removeProduct(Product toRemove) throws ProductNotFoundException {

        if(productRepository.existsProductById(toRemove.getId()) ) {
            productRepository.deleteById(toRemove.getId());
        }else  {
            throw new ProductNotFoundException();
        }

    }

}
