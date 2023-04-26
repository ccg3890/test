import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SpecFormService, SpecFormGroup } from './spec-form.service';
import { ISpec } from '../spec.model';
import { SpecService } from '../service/spec.service';
import { CategoryStatus } from 'app/entities/enumerations/category-status.model';
import { CategoryGrade } from 'app/entities/enumerations/category-grade.model';

@Component({
  selector: 'jhi-spec-update',
  templateUrl: './spec-update.component.html',
})
export class SpecUpdateComponent implements OnInit {
  isSaving = false;
  spec: ISpec | null = null;
  categoryStatusValues = Object.keys(CategoryStatus);
  categoryGradeValues = Object.keys(CategoryGrade);

  editForm: SpecFormGroup = this.specFormService.createSpecFormGroup();

  constructor(protected specService: SpecService, protected specFormService: SpecFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spec }) => {
      this.spec = spec;
      if (spec) {
        this.updateForm(spec);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const spec = this.specFormService.getSpec(this.editForm);
    if (spec.id !== null) {
      this.subscribeToSaveResponse(this.specService.update(spec));
    } else {
      this.subscribeToSaveResponse(this.specService.create(spec));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpec>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(spec: ISpec): void {
    this.spec = spec;
    this.specFormService.resetForm(this.editForm, spec);
  }
}
