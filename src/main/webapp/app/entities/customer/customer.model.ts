import { ICart } from 'app/entities/cart/cart.model';
import { IOrder } from 'app/entities/order/order.model';
import { IProductReview } from 'app/entities/product-review/product-review.model';
import { IAddress } from 'app/entities/address/address.model';

export interface ICustomer {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  shoppingCart?: ICart | null;
  orders?: IOrder[] | null;
  reviews?: IProductReview[] | null;
  addresses?: IAddress[] | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public shoppingCart?: ICart | null,
    public orders?: IOrder[] | null,
    public reviews?: IProductReview[] | null,
    public addresses?: IAddress[] | null
  ) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
