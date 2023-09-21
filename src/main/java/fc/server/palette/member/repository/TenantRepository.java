package fc.server.palette.member.repository;


import fc.server.palette.member.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

}
