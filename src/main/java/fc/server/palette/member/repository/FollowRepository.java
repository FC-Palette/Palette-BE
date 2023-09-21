package fc.server.palette.member.repository;


import fc.server.palette.member.entity.Follow;
import fc.server.palette.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollowedId(Long followedId);
    List<Follow> findByFollowingId(Long followingId);

    Optional<Follow> findByFollowedIdAndFollowingId(Long followedId, Long followingId);

    @Modifying
    @Transactional
    void deleteByFollowedIdAndFollowingId(Long followedId, Long followingId);

    long countByFollowedId(Long memberId);

    long countByFollowingId(Long memberId);
}
