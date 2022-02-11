export interface IAddItemToCartModel {
  productId?: number;
  quantity?: number;
}

export class AddItemToCartModel implements IAddItemToCartModel {
  constructor(public productId?: number, public quantity?: number)
  {}
}
