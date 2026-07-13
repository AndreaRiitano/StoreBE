package org.esamepsw.store.controllers;

import org.esamepsw.store.entities.Product;
import org.esamepsw.store.services.ProductService;
import org.esamepsw.store.utilities.exceptions.product.ProductAlreadyExistsException;
import org.esamepsw.store.utilities.exceptions.product.ProductCategoryNotFound;
import org.esamepsw.store.utilities.exceptions.product.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin', 'seller')")
    public ResponseEntity addProduct(@RequestBody Product product) {

        try{
           Product added = productService.addProduct(product);
            return new ResponseEntity<>(added ,HttpStatus.CREATED);
        }catch(ProductAlreadyExistsException e){
            return new ResponseEntity<>("Product already exists", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin', 'seller')")
    public ResponseEntity updateProduct(@RequestBody Product product) {
        try{
            Product updated = productService.updateProduct(product);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }catch(ProductNotFoundException e){
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> allProducts = productService.findAll();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("name") String name) {
        List<Product> products = productService.findByNameContainingIgnoreCase(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Product>> getAllProductsByCategory(String category) {

        try{

            List<Product> categoryProducts = productService.findByCategory(category);
            return new ResponseEntity<>(categoryProducts, HttpStatus.OK);
        }catch (ProductCategoryNotFound e){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/search/name")
    public ResponseEntity getProductByName(@RequestParam("name") String name) {
        List<Product> products = productService.findByName(name);
        if(products.isEmpty()){
            return new ResponseEntity<>("Product not found", HttpStatus.OK);
        }else {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
    }

    @GetMapping("/search/code")
    public ResponseEntity getProductByCode(@RequestParam("code") String code) {

        try{
            Product product = productService.findByCode(code);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }catch (ProductNotFoundException e){
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'seller')")
    public ResponseEntity deleteProduct(@PathVariable("id") long id) {

        try {
            productService.removeProduct(productService.findById(id));
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ProductNotFoundException e){
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
        try {
            Product prod = productService.findById(id);
            return new ResponseEntity<>(prod, HttpStatus.OK);
        }catch (ProductNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
