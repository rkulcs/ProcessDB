package processdb.backend.processes;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProcessCommentRepository extends Repository<ProcessComment, Long> {

    List<ProcessComment> findAllByProcess(Process process);

    ProcessComment save(ProcessComment comment);

    ProcessComment findById(Long id);

    @Modifying
    Long deleteById(Long id);
}
