package org.esamepsw.store.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.esamepsw.store.entities.Product;
import org.esamepsw.store.entities.ProductInPurchase;
import org.esamepsw.store.entities.Purchase;
import org.esamepsw.store.entities.User;
import org.esamepsw.store.repositories.ProductInPurchaseRepository;
import org.esamepsw.store.repositories.ProductRepository;
import org.esamepsw.store.repositories.PurchaseRepository;
import org.esamepsw.store.repositories.UserRepository;
import org.esamepsw.store.utilities.exceptions.product.ProductNotFoundException;
import org.esamepsw.store.utilities.exceptions.purchase.QuantityUnavailableException;
import org.esamepsw.store.utilities.dto.PipAddRequest;
import org.esamepsw.store.utilities.exceptions.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private ProductInPurchaseRepository productInPurchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesByUser( User user) throws UserNotFoundException {
        if ( !userRepository.existsById(user.getId()) ) {
            throw new UserNotFoundException();
        }
        return purchaseRepository.findByUser(user);
    }

    public List<ProductInPurchase> getProductInPurchaseByUser( User user) throws UserNotFoundException {
        if ( !userRepository.existsById(user.getId()) ) {
            throw new UserNotFoundException();
        }
        return productInPurchaseRepository.findProductInPurchaseByUser(user);
    }

    @Transactional(readOnly = false)
    public Purchase addPurchase(Purchase incomingPurchase) {

        //da mettere altro per controllo utente

        User user = entityManager.find(User.class, incomingPurchase.getUser().getId());
        if (user == null) {
            throw new UserNotFoundException();
        }

        Purchase newPurchase = new Purchase();
        newPurchase.setUser(user);

        if (incomingPurchase.getProductInPurchase() != null) {
            for (ProductInPurchase incomingPip : incomingPurchase.getProductInPurchase()) {

                Product product = entityManager.find(
                        Product.class,
                        incomingPip.getProduct().getId(),
                        LockModeType.PESSIMISTIC_WRITE
                );

                if (product == null) {
                    throw new ProductNotFoundException();
                }

                if (product.getQuantity() < incomingPip.getQuantity()) {
                    throw new QuantityUnavailableException();
                }

                product.setQuantity(product.getQuantity() - incomingPip.getQuantity());

                ProductInPurchase newPip = new ProductInPurchase();
                newPip.setPurchase(newPurchase);
                newPip.setProduct(product);
                newPip.setQuantity(incomingPip.getQuantity());

                newPurchase.getProductInPurchase().add(newPip);
            }
        }
        entityManager.persist(newPurchase);

        return newPurchase;
    }


    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Transactional(readOnly = false)
    public ProductInPurchase addProductInPurchase(PipAddRequest incomingPurchase) {
        System.out.println(incomingPurchase.getKeycloakId()+"ciao");
        User user = userRepository.findByKeycloakId(incomingPurchase.getKeycloakId());
        ProductInPurchase toAdd = new ProductInPurchase();
        toAdd.setUser(user);
        toAdd.setProduct(incomingPurchase.getProduct());
        toAdd.setQuantity(incomingPurchase.getQuantity());
        if(!userRepository.existsByKeycloakId(incomingPurchase.getKeycloakId())) {
            throw new UserNotFoundException();
        }
        if(!productRepository.existsProductById(toAdd.getProduct().getId())) {
            throw new ProductNotFoundException();
        }

       return productInPurchaseRepository.save(toAdd);
    }
}
