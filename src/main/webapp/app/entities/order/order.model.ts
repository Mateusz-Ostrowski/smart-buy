import { ICustomer } from 'app/entities/customer/customer.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';

export interface IOrder {
  id?: number;
  uuid?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  city?: string | null;
  postalCode?: string | null;
  street?: string | null;
  buildingNo?: string | null;
  doorNo?: string | null;
  description?: string | null;
  customer?: ICustomer | null;
  products?: IOrderItem[] | null;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public uuid?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public city?: string | null,
    public postalCode?: string | null,
    public street?: string | null,
    public buildingNo?: string | null,
    public doorNo?: string | null,
    public description?: string | null,
    public customer?: ICustomer | null,
    public products?: IOrderItem[] | null
  ) {}
}

export function getOrderIdentifier(order: IOrder): number | undefined {
  return order.id;
}
