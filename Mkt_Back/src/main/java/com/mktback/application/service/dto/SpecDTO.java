package com.mktback.application.service.dto;

import com.mktback.application.domain.enumeration.CategoryGrade;
import com.mktback.application.domain.enumeration.CategoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mktback.application.domain.Spec} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecDTO implements Serializable {

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
    @Schema(description = "grade")
    private LocalDate createDt;

    private LocalDate modifyDt;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecDTO)) {
            return false;
        }

        SpecDTO specDTO = (SpecDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, specDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", parentId='" + getParentId() + "'" +
            ", grade='" + getGrade() + "'" +
            ", createDt='" + getCreateDt() + "'" +
            ", modifyDt='" + getModifyDt() + "'" +
            "}";
    }
}
