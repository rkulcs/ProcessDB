package processdb.backend.processes;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import processdb.backend.users.User;

import java.util.Date;

@Entity
@Table(name = "processComments")
public class ProcessComment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dateWritten;

    @ManyToOne
    private User author;

    @NotNull
    @ManyToOne
    private Process process;

    @NotNull
    private Boolean isSafe;

    private String info;

    public ProcessComment() {}

    public ProcessComment(Boolean isSafe, String info) {

        this.isSafe = isSafe;
        this.info = info;
        this.dateWritten = new Date();
    }

    public ProcessComment(Process process, Boolean isSafe, String info) {

        this.process = process;
        this.isSafe = isSafe;
        this.info = info;
        this.dateWritten = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateWritten() {
        return dateWritten;
    }

    public void setDateWritten(Date dateWritten) {
        this.dateWritten = dateWritten;
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

    //===== Serialization Getters =====//

    @JsonProperty("author")
    public String getAuthorId() {
        return author.getUsername();
    }

    @JsonProperty("process")
    public Long getProcessId() {
        return process.getId();
    }
}
