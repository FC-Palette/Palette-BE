package fc.server.palette.member.repository;


import fc.server.palette.member.entity.Follow;
import fc.server.palette.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollowing(Member following);
    List<Follow> findByFollowed(Member followed);

    Optional<Follow> findByFollowingAndFollowed(Member following, Member followed);

    long countByFollowed_Id(Long memberId);

    long countByFollowing_Id(Long memberId);
}
