import dayjs from 'dayjs/esm';

import { CategoryStatus } from 'app/entities/enumerations/category-status.model';
import { CategoryGrade } from 'app/entities/enumerations/category-grade.model';

import { ISpec, NewSpec } from './spec.model';

export const sampleWithRequiredData: ISpec = {
  id: '473203c2-def0-4ffa-9322-04699f7dc408',
};

export const sampleWithPartialData: ISpec = {
  id: 'b24f6b9e-fd29-41dd-8355-dbc70fe6e88a',
  name: 'Frozen niches invoice',
  grade: CategoryGrade['C'],
  modifyDt: dayjs('2023-04-25'),
};

export const sampleWithFullData: ISpec = {
  id: 'bdba5f8b-04cb-4726-8b2e-d3ba462be504',
  name: 'Street',
  status: CategoryStatus['Inactive'],
  parentId: 'maximized Cotton',
  grade: CategoryGrade['B'],
  createDt: dayjs('2023-04-25'),
  modifyDt: dayjs('2023-04-25'),
};

export const sampleWithNewData: NewSpec = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
