import { ICustomer } from 'app/entities/customer/customer.model';
import { ICartItem } from 'app/entities/cart-item/cart-item.model';

export interface ICart {
  id?: number;
  customer?: ICustomer | null;
  items?: ICartItem[] | null;
}

export class Cart implements ICart {
  constructor(public id?: number, public customer?: ICustomer | null, public items?: ICartItem[] | null) {}
}

export function getCartIdentifier(cart: ICart): number | undefined {
  return cart.id;
}
