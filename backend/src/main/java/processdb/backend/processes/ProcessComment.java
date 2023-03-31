package processdb.backend.processes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import processdb.backend.users.User;

@Entity
@Table(name = "processComments")
public class ProcessComment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private User author;

    @NotNull
    @ManyToOne
    private Process process;

    @NotNull
    private Boolean isSafe;

    private String info;

    public ProcessComment() {}

    public ProcessComment(Process process, Boolean isSafe, String info) {

        this.process = process;
        this.isSafe = isSafe;
        this.info = info;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Boolean getSafe() {
        return isSafe;
    }

    public void setSafe(Boolean safe) {
        isSafe = safe;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
