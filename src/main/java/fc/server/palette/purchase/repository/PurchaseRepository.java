package fc.server.palette.purchase.repository;


import fc.server.palette.purchase.entity.Purchase;
import fc.server.palette.purchase.entity.type.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Modifying
    @Query("update Purchase p set p.isClosing=true where p.endDate < :now")
    int bulkUpdateClosing(@Param("now") LocalDate now);

    List<Purchase> findAllByCategoryAndPriceBetween(Category category, Integer minPrice, Integer maxPrice);
}