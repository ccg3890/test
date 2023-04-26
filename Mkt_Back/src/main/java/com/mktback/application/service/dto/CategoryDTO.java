package com.mktback.application.service.dto;

import com.mktback.application.domain.enumeration.CategoryGrade;
import com.mktback.application.domain.enumeration.CategoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mktback.application.domain.Category} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryDTO implements Serializable {

    @NotNull
    private String id;

    /**
     * category code id
     */
    @Size(max = 200)
    @Schema(description = "category code id")
    private String name;

    /**
     * name
     */
    @Schema(description = "name")
    private CategoryStatus status;

    private String parentId;

    private CategoryGrade grade;

    /**
     * grade
     */
    @NotNull
    @Schema(description = "grade", required = true)
    private String createUser;

    private String modifyUser;

    @NotNull
    private LocalDate createDt;

    private LocalDate modifyDt;

    private UserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public CategoryGrade getGrade() {
        return grade;
    }

    public void setGrade(CategoryGrade grade) {
        this.grade = grade;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public LocalDate getCreateDt() {
        return createDt;
    }

    public void setCreateDt(LocalDate createDt) {
        this.createDt = createDt;
    }

    public LocalDate getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(LocalDate modifyDt) {
        this.modifyDt = modifyDt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDTO)) {
            return false;
        }

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", parentId='" + getParentId() + "'" +
            ", grade='" + getGrade() + "'" +
            ", createUser='" + getCreateUser() + "'" +
            ", modifyUser='" + getModifyUser() + "'" +
            ", createDt='" + getCreateDt() + "'" +
            ", modifyDt='" + getModifyDt() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
