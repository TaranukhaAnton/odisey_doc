import { IDodument } from 'app/shared/model/dodument.model';

export interface IAttachment {
  id?: number;
  description?: string | null;
  fileContentType?: string | null;
  file?: string | null;
  dodument?: IDodument | null;
}

export const defaultValue: Readonly<IAttachment> = {};
