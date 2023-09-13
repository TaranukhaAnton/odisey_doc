import dayjs from 'dayjs';
import { IDodument } from 'app/shared/model/dodument.model';

export interface IComment {
  id?: number;
  text?: string | null;
  date?: string | null;
  author?: string | null;
  dodument?: IDodument | null;
}

export const defaultValue: Readonly<IComment> = {};
