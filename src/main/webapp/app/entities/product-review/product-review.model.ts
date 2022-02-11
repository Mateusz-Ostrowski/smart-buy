import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IProductReview {
  id?: number;
  text?: string | null;
  rating?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  product?: IProduct | null;
  customer?: ICustomer | null;
}

export class ProductReview implements IProductReview {
  constructor(
    public id?: number,
    public text?: string | null,
    public rating?: number | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public product?: IProduct | null,
    public customer?: ICustomer | null
  ) {}
}

export function getProductReviewIdentifier(productReview: IProductReview): number | undefined {
  return productReview.id;
}
