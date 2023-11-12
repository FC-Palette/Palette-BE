package fc.server.palette.secondhand.repository;

import fc.server.palette.purchase.entity.type.Category;
import fc.server.palette.secondhand.entity.Secondhand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SecondhandRespository extends JpaRepository<Secondhand, Long> {

    @Query("select s from Secondhand s where s.member.id = ?1 and s.id <> ?2")
    List<Secondhand> findAllByMemberIdAndExcludeId(Long memberId, Long idToExclude);

    List<Secondhand> findAllByCategoryIn(List<Category> categories);
}
