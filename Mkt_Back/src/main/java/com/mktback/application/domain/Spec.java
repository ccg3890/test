package com.mktback.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mktback.application.domain.enumeration.CategoryGrade;
import com.mktback.application.domain.enumeration.CategoryStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A Spec.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@Table(name = "tb_mkt_spec")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Spec implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * category code id
     */
    @Size(max = 200)
    @Column(name = "name", length = 200)
    private String name;

    /**
     * name
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CategoryStatus status;

    @Column(name = "parent_id")
    private String parentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private CategoryGrade grade;

    /**
     * grade
     */
    @Column(name = "create_dt")
    private LocalDate createDt;

    @Column(name = "modify_dt")
    private LocalDate modifyDt;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Spec id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Spec name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryStatus getStatus() {
        return this.status;
    }

    public Spec status(CategoryStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public String getParentId() {
        return this.parentId;
    }

    public Spec parentId(String parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public CategoryGrade getGrade() {
        return this.grade;
    }

    public Spec grade(CategoryGrade grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(CategoryGrade grade) {
        this.grade = grade;
    }

    public LocalDate getCreateDt() {
        return this.createDt;
    }

    public Spec createDt(LocalDate createDt) {
        this.setCreateDt(createDt);
        return this;
    }

    public void setCreateDt(LocalDate createDt) {
        this.createDt = createDt;
    }

    public LocalDate getModifyDt() {
        return this.modifyDt;
    }

    public Spec modifyDt(LocalDate modifyDt) {
        this.setModifyDt(modifyDt);
        return this;
    }

    public void setModifyDt(LocalDate modifyDt) {
        this.modifyDt = modifyDt;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Spec setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spec)) {
            return false;
        }
        return id != null && id.equals(((Spec) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Spec{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", parentId='" + getParentId() + "'" +
            ", grade='" + getGrade() + "'" +
            ", createDt='" + getCreateDt() + "'" +
            ", modifyDt='" + getModifyDt() + "'" +
            "}";
    }
}
