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
 * A Category.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@Table(name = "tb_mkt_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable, Persistable<String> {

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
    @NotNull
    @Column(name = "create_user", nullable = false)
    private String createUser;

    @Column(name = "modify_user")
    private String modifyUser;

    @NotNull
    @Column(name = "create_dt", nullable = false)
    private LocalDate createDt;

    @Column(name = "modify_dt")
    private LocalDate modifyDt;

    @Transient
    private boolean isPersisted;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Category id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryStatus getStatus() {
        return this.status;
    }

    public Category status(CategoryStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public String getParentId() {
        return this.parentId;
    }

    public Category parentId(String parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public CategoryGrade getGrade() {
        return this.grade;
    }

    public Category grade(CategoryGrade grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(CategoryGrade grade) {
        this.grade = grade;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public Category createUser(String createUser) {
        this.setCreateUser(createUser);
        return this;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return this.modifyUser;
    }

    public Category modifyUser(String modifyUser) {
        this.setModifyUser(modifyUser);
        return this;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public LocalDate getCreateDt() {
        return this.createDt;
    }

    public Category createDt(LocalDate createDt) {
        this.setCreateDt(createDt);
        return this;
    }

    public void setCreateDt(LocalDate createDt) {
        this.createDt = createDt;
    }

    public LocalDate getModifyDt() {
        return this.modifyDt;
    }

    public Category modifyDt(LocalDate modifyDt) {
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

    public Category setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", parentId='" + getParentId() + "'" +
            ", grade='" + getGrade() + "'" +
            ", createUser='" + getCreateUser() + "'" +
            ", modifyUser='" + getModifyUser() + "'" +
            ", createDt='" + getCreateDt() + "'" +
            ", modifyDt='" + getModifyDt() + "'" +
            "}";
    }
}
