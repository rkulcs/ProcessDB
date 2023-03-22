package processdb.backend.processes;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProcessRepository extends Repository<Process, Long> {

    List<Process> findAll();

    Process save(Process process);

    Process findById(Long id);

    @Modifying
    Long deleteById(Long id);
}
