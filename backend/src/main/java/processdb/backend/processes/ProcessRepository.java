package processdb.backend.processes;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface ProcessRepository extends Repository<Process, Long> {

    List<Process> findAll();

    Process save(Process process);

    Process findById(Long id);
}
