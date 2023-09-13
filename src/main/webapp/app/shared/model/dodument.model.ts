import dayjs from 'dayjs';
import { IComment } from 'app/shared/model/comment.model';
import { IAttachment } from 'app/shared/model/attachment.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IDodument {
  id?: number;
  description?: string | null;
  actionsDescription?: string | null;
  date?: string | null;
  status?: Status | null;
  comments?: IComment[] | null;
  attachments?: IAttachment[] | null;
}

export const defaultValue: Readonly<IDodument> = {};
