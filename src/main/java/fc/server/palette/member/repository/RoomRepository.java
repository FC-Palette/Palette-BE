package fc.server.palette.member.repository;


import fc.server.palette.member.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
