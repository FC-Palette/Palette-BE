package fc.server.palette.member.repository;


import fc.server.palette.member.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
