package org.esamepsw.store.repositories;

import org.esamepsw.store.entities.Purchase;
import org.esamepsw.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByBuyer(User buyer);
    List<Purchase> findByPurchaseTime(Date date);
    List<Purchase> findByBuyerBetween(Date startDate, Date endDate, User user);

}
