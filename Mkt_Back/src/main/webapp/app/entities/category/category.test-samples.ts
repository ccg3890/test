import dayjs from 'dayjs/esm';

import { CategoryStatus } from 'app/entities/enumerations/category-status.model';
import { CategoryGrade } from 'app/entities/enumerations/category-grade.model';

import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: '0aaf2144-edc0-4adb-933d-efd5dc5e205d',
  createUser: 'Borders bus',
  createDt: dayjs('2023-04-25'),
};

export const sampleWithPartialData: ICategory = {
  id: '1b61d999-b9e6-480c-b6dd-7bb57ea64269',
  name: 'Music',
  status: CategoryStatus['Active'],
  createUser: 'Brand compressing Pizza',
  createDt: dayjs('2023-04-25'),
};

export const sampleWithFullData: ICategory = {
  id: 'fe9bb54b-4157-42ef-ab01-ad7a73f91ca8',
  name: 'Plastic Ball Chair',
  status: CategoryStatus['Inactive'],
  parentId: 'Computer Rustic mesh',
  grade: CategoryGrade['B'],
  createUser: 'auxiliary',
  modifyUser: 'Branding',
  createDt: dayjs('2023-04-26'),
  modifyDt: dayjs('2023-04-25'),
};

export const sampleWithNewData: NewCategory = {
  createUser: 'Spain Barbados',
  createDt: dayjs('2023-04-26'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
