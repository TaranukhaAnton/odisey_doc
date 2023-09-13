package ua.gov.odisey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ua.gov.odisey.domain.enumeration.Status;

/**
 * A Dodument.
 */
@Entity
@Table(name = "dodument")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dodument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "actions_description")
    private String actionsDescription;

    @Column(name = "date")
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "dodument")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dodument" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "dodument")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dodument" }, allowSetters = true)
    private Set<Attachment> attachments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dodument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Dodument description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionsDescription() {
        return this.actionsDescription;
    }

    public Dodument actionsDescription(String actionsDescription) {
        this.setActionsDescription(actionsDescription);
        return this;
    }

    public void setActionsDescription(String actionsDescription) {
        this.actionsDescription = actionsDescription;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Dodument date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return this.status;
    }

    public Dodument status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setDodument(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setDodument(this));
        }
        this.comments = comments;
    }

    public Dodument comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Dodument addComment(Comment comment) {
        this.comments.add(comment);
        comment.setDodument(this);
        return this;
    }

    public Dodument removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setDodument(null);
        return this;
    }

    public Set<Attachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        if (this.attachments != null) {
            this.attachments.forEach(i -> i.setDodument(null));
        }
        if (attachments != null) {
            attachments.forEach(i -> i.setDodument(this));
        }
        this.attachments = attachments;
    }

    public Dodument attachments(Set<Attachment> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public Dodument addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setDodument(this);
        return this;
    }

    public Dodument removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setDodument(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dodument)) {
            return false;
        }
        return id != null && id.equals(((Dodument) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dodument{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", actionsDescription='" + getActionsDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
