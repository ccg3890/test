import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategory, NewCategory } from '../category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategory for edit and NewCategoryFormGroupInput for create.
 */
type CategoryFormGroupInput = ICategory | PartialWithRequiredKeyOf<NewCategory>;

type CategoryFormDefaults = Pick<NewCategory, 'id'>;

type CategoryFormGroupContent = {
  id: FormControl<ICategory['id'] | NewCategory['id']>;
  name: FormControl<ICategory['name']>;
  status: FormControl<ICategory['status']>;
  parentId: FormControl<ICategory['parentId']>;
  grade: FormControl<ICategory['grade']>;
  createUser: FormControl<ICategory['createUser']>;
  modifyUser: FormControl<ICategory['modifyUser']>;
  createDt: FormControl<ICategory['createDt']>;
  modifyDt: FormControl<ICategory['modifyDt']>;
  user: FormControl<ICategory['user']>;
};

export type CategoryFormGroup = FormGroup<CategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoryFormService {
  createCategoryFormGroup(category: CategoryFormGroupInput = { id: null }): CategoryFormGroup {
    const categoryRawValue = {
      ...this.getFormDefaults(),
      ...category,
    };
    return new FormGroup<CategoryFormGroupContent>({
      id: new FormControl(
        { value: categoryRawValue.id, disabled: categoryRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(categoryRawValue.name, {
        validators: [Validators.maxLength(200)],
      }),
      status: new FormControl(categoryRawValue.status),
      parentId: new FormControl(categoryRawValue.parentId),
      grade: new FormControl(categoryRawValue.grade),
      createUser: new FormControl(categoryRawValue.createUser, {
        validators: [Validators.required],
      }),
      modifyUser: new FormControl(categoryRawValue.modifyUser),
      createDt: new FormControl(categoryRawValue.createDt, {
        validators: [Validators.required],
      }),
      modifyDt: new FormControl(categoryRawValue.modifyDt),
      user: new FormControl(categoryRawValue.user),
    });
  }

  getCategory(form: CategoryFormGroup): ICategory | NewCategory {
    return form.getRawValue() as ICategory | NewCategory;
  }

  resetForm(form: CategoryFormGroup, category: CategoryFormGroupInput): void {
    const categoryRawValue = { ...this.getFormDefaults(), ...category };
    form.reset(
      {
        ...categoryRawValue,
        id: { value: categoryRawValue.id, disabled: categoryRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategoryFormDefaults {
    return {
      id: null,
    };
  }
}
