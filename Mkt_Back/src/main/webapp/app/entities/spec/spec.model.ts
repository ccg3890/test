import dayjs from 'dayjs/esm';
import { CategoryStatus } from 'app/entities/enumerations/category-status.model';
import { CategoryGrade } from 'app/entities/enumerations/category-grade.model';

export interface ISpec {
  id: string;
  name?: string | null;
  status?: CategoryStatus | null;
  parentId?: string | null;
  grade?: CategoryGrade | null;
  createDt?: dayjs.Dayjs | null;
  modifyDt?: dayjs.Dayjs | null;
}

export type NewSpec = Omit<ISpec, 'id'> & { id: null };
