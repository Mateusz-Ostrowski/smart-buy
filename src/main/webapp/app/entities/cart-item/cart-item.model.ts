import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { ICart } from 'app/entities/cart/cart.model';

export interface ICartItem {
  id?: number;
  price?: number | null;
  quantity?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  product?: IProduct | null;
  cart?: ICart | null;
}

export class CartItem implements ICartItem {
  constructor(
    public id?: number,
    public price?: number | null,
    public quantity?: number | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public product?: IProduct | null,
    public cart?: ICart | null
  ) {}
}

export function getCartItemIdentifier(cartItem: ICartItem): number | undefined {
  return cartItem.id;
}
