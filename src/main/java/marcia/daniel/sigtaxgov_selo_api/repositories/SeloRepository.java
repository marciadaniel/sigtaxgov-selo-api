package marcia.daniel.sigtaxgov_selo_api.repositories;

import marcia.daniel.sigtaxgov_selo_api.enums.Status;
import marcia.daniel.sigtaxgov_selo_api.models.Selo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeloRepository extends JpaRepository<Selo, Long> {


    List<Selo> findByStatus(Status status);

    Optional<Selo> findByCodigo(String codigo);
}
