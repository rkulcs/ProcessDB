package processdb.backend.processes;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProcessCommentRepository {

    ProcessComment save();

    Process findById(Long id);

    @Modifying
    Long deleteById(Long id);
}
