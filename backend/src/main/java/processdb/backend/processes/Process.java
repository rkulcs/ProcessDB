package processdb.backend.processes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import processdb.backend.users.User;

import java.io.Serializable;

@Entity
@Table(name = "processes")
public class Process implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private User addedBy;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String filename;

    @NotNull
    @NotBlank
    private String os;

    private String description;

    public Process() {}

    public Process(String name, String filename, String os, User addedBy) {

        this.name = name;
        this.filename = filename;
        this.os = os;
        this.addedBy = addedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOS() {
        return os;
    }

    public void setOS(String os) {
        this.os = os;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public void copyNonIdFieldsOf(Process process) {

        this.name = process.name;
        this.filename = process.filename;
        this.os = process.os;
        this.description = process.description;
    }
}
