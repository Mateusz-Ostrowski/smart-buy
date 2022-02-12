import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { observable, Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICart, getCartIdentifier } from '../cart.model';
import { AddItemToCartModel, IAddItemToCartModel } from '../add-item-to-cart.model';
import { ICartItem } from '../../cart-item/cart-item.model';

export type EntityResponseType = HttpResponse<ICart>;
export type EntityArrayResponseType = HttpResponse<ICart[]>;

@Injectable({ providedIn: 'root' })
export class CartService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/carts');
  protected cartUrl = this.applicationConfigService.getEndpointFor('api/cart');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cart: ICart): Observable<EntityResponseType> {
    return this.http.post<ICart>(this.resourceUrl, cart, { observe: 'response' });
  }

  update(cart: ICart): Observable<EntityResponseType> {
    return this.http.put<ICart>(`${this.resourceUrl}/${getCartIdentifier(cart) as number}`, cart, { observe: 'response' });
  }

  partialUpdate(cart: ICart): Observable<EntityResponseType> {
    return this.http.patch<ICart>(`${this.resourceUrl}/${getCartIdentifier(cart) as number}`, cart, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICart>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICart[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCartToCollectionIfMissing(cartCollection: ICart[], ...cartsToCheck: (ICart | null | undefined)[]): ICart[] {
    const carts: ICart[] = cartsToCheck.filter(isPresent);
    if (carts.length > 0) {
      const cartCollectionIdentifiers = cartCollection.map(cartItem => getCartIdentifier(cartItem)!);
      const cartsToAdd = carts.filter(cartItem => {
        const cartIdentifier = getCartIdentifier(cartItem);
        if (cartIdentifier == null || cartCollectionIdentifiers.includes(cartIdentifier)) {
          return false;
        }
        cartCollectionIdentifiers.push(cartIdentifier);
        return true;
      });
      return [...cartsToAdd, ...cartCollection];
    }
    return cartCollection;
  }
  addItemToCart(productId: number | undefined, quantity: number): void {
    this.http.post<IAddItemToCartModel>(this.cartUrl, new AddItemToCartModel(productId, quantity)).subscribe(
      value => {
        alert('Item successfully added to cart!');
      },
      error => {
        alert('Cannot add more items to cart than there are in stock');
      }
    );
    //return this.http.post<ICartItem>(this.resourceUrl,new AddItemToCartModel(productId,quantity), { observe: 'response' }).pipe();
  }

  getCurrentUserCartItems(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICartItem[]>(this.cartUrl, { params: options, observe: 'response' });
  }
}
