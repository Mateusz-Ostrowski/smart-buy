import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';

export interface IFileInfo {
  id?: number;
  fileName?: string;
  originalFileName?: string | null;
  fileSize?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  imageOf?: IProduct | null;
  fileIds?: number[] | null;
}

export class FileInfo implements IFileInfo {
  constructor(
    public id?: number,
    public fileName?: string,
    public originalFileName?: string | null,
    public fileSize?: number | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public imageOf?: IProduct | null,
    public fileIds?: number[] | null
  ) {}
}

export function getFileInfoIdentifier(fileInfo: IFileInfo): number | undefined {
  return fileInfo.id;
}
