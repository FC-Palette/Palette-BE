package fc.server.palette.purchase.repository;

import fc.server.palette.purchase.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchaseBookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByPurchase_id(Long productId);

    Optional<Bookmark> findByMemberIdAndPurchaseId(Long memberId, Long purchaseId);
}
