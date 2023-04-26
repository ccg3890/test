import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { CategoryStatus } from 'app/entities/enumerations/category-status.model';
import { CategoryGrade } from 'app/entities/enumerations/category-grade.model';

export interface ICategory {
  id: string;
  name?: string | null;
  status?: CategoryStatus | null;
  parentId?: string | null;
  grade?: CategoryGrade | null;
  createUser?: string | null;
  modifyUser?: string | null;
  createDt?: dayjs.Dayjs | null;
  modifyDt?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
