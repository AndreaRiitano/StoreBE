package org.esamepsw.store.services;



import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.esamepsw.store.entities.Product;
import org.esamepsw.store.repositories.ProductRepository;
import org.esamepsw.store.utilities.exceptions.product.ProductAlreadyExistsException;
import org.esamepsw.store.utilities.exceptions.product.ProductCategoryNotFound;
import org.esamepsw.store.utilities.exceptions.product.ProductNotFoundException;
import org.esamepsw.store.utilities.exceptions.product.ProductNotValidException;
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
        if(productRepository.existsByCategory(category)) {
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
    @Transactional(readOnly = true)
    public List<Product> findByNameContainingIgnoreCase(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = false)
    public Product addProduct(Product product) throws ProductAlreadyExistsException {

        if(productRepository.existsProductByCode(product.getCode())) {
            throw new ProductAlreadyExistsException();
        }
        if(product.getName()!=null && product.getCode()!=null && product.getCategory()!=null
                && product.getDescription()!=null && product.getPrice()>0.0
                && product.getQuantity()>0 && product.getImgUrl()!=null) {

             return productRepository.save(product);
        }else{
            throw new ProductNotValidException();

        }

    }

    @Transactional(readOnly = false)
    public Product updateProduct(Product product) throws ProductNotFoundException {

        if(! productRepository.existsProductById(product.getId()) || ! productRepository.existsProductByCode(product.getCode())) {

            throw new ProductNotFoundException();
        }

        Product originalProduct = productRepository.findById(product.getId()).get();
        entityManager.lock(originalProduct, LockModeType.OPTIMISTIC);

        if(product.getName()!=null) {
            originalProduct.setName(product.getName());
        }
        if(product.getDescription()!=null) {
            originalProduct.setDescription(product.getDescription());
        }
        if(product.getCode()!=null) {
            originalProduct.setCode(product.getCode());
        }
        if(product.getCategory()!=null) {
            originalProduct.setCategory(product.getCategory());
        }
        if(product.getPrice()>0.0){
            originalProduct.setPrice(product.getPrice());
        }
        if(product.getQuantity()>=0){
            originalProduct.setQuantity(product.getQuantity());
        }
        if(product.getImgUrl()!=null) {
            originalProduct.setImgUrl(product.getImgUrl());
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
