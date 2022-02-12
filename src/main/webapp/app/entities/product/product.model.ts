import dayjs from 'dayjs/esm';
import { ICategory } from 'app/entities/category/category.model';
import { IFileInfo } from 'app/entities/file-info/file-info.model';
import { IProductReview } from 'app/entities/product-review/product-review.model';
import { ProductStatus } from 'app/entities/enumerations/product-status.model';
import { FileSystemEntry, NgxFileDropEntry } from 'ngx-file-drop';

export interface IProduct {
  id?: number;
  name?: string | null;
  price?: number | null;
  quantity?: number | null;
  discountPrice?: number | null;
  status?: ProductStatus | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  category?: ICategory | null;
  images?: IFileInfo[] | null;
  reviews?: IProductReview[] | null;
  fileIds?: number[] | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public price?: number | null,
    public quantity?: number | null,
    public discountPrice?: number | null,
    public status?: ProductStatus | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public category?: ICategory | null,
    public images?: IFileInfo[] | null,
    public reviews?: IProductReview[] | null,
    public fileIds?: number[] | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
