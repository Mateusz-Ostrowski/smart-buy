import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IOrderItem {
  id?: number;
  price?: number | null;
  quantity?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  product?: IProduct | null;
  order?: IOrder | null;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public price?: number | null,
    public quantity?: number | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public product?: IProduct | null,
    public order?: IOrder | null
  ) {}
}

export function getOrderItemIdentifier(orderItem: IOrderItem): number | undefined {
  return orderItem.id;
}
