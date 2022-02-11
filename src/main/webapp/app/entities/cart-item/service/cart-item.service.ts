import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICartItem, getCartItemIdentifier } from '../cart-item.model';

export type EntityResponseType = HttpResponse<ICartItem>;
export type EntityArrayResponseType = HttpResponse<ICartItem[]>;

@Injectable({ providedIn: 'root' })
export class CartItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cart-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cartItem: ICartItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cartItem);
    return this.http
      .post<ICartItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cartItem: ICartItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cartItem);
    return this.http
      .put<ICartItem>(`${this.resourceUrl}/${getCartItemIdentifier(cartItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cartItem: ICartItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cartItem);
    return this.http
      .patch<ICartItem>(`${this.resourceUrl}/${getCartItemIdentifier(cartItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICartItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICartItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCartItemToCollectionIfMissing(cartItemCollection: ICartItem[], ...cartItemsToCheck: (ICartItem | null | undefined)[]): ICartItem[] {
    const cartItems: ICartItem[] = cartItemsToCheck.filter(isPresent);
    if (cartItems.length > 0) {
      const cartItemCollectionIdentifiers = cartItemCollection.map(cartItemItem => getCartItemIdentifier(cartItemItem)!);
      const cartItemsToAdd = cartItems.filter(cartItemItem => {
        const cartItemIdentifier = getCartItemIdentifier(cartItemItem);
        if (cartItemIdentifier == null || cartItemCollectionIdentifiers.includes(cartItemIdentifier)) {
          return false;
        }
        cartItemCollectionIdentifiers.push(cartItemIdentifier);
        return true;
      });
      return [...cartItemsToAdd, ...cartItemCollection];
    }
    return cartItemCollection;
  }

  protected convertDateFromClient(cartItem: ICartItem): ICartItem {
    return Object.assign({}, cartItem, {
      createdAt: cartItem.createdAt?.isValid() ? cartItem.createdAt.toJSON() : undefined,
      updatedAt: cartItem.updatedAt?.isValid() ? cartItem.updatedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cartItem: ICartItem) => {
        cartItem.createdAt = cartItem.createdAt ? dayjs(cartItem.createdAt) : undefined;
        cartItem.updatedAt = cartItem.updatedAt ? dayjs(cartItem.updatedAt) : undefined;
      });
    }
    return res;
  }
}
