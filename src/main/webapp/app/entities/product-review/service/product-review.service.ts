import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductReview, getProductReviewIdentifier } from '../product-review.model';

export type EntityResponseType = HttpResponse<IProductReview>;
export type EntityArrayResponseType = HttpResponse<IProductReview[]>;

@Injectable({ providedIn: 'root' })
export class ProductReviewService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-reviews');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productReview: IProductReview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productReview);
    return this.http
      .post<IProductReview>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productReview: IProductReview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productReview);
    return this.http
      .put<IProductReview>(`${this.resourceUrl}/${getProductReviewIdentifier(productReview) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(productReview: IProductReview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productReview);
    return this.http
      .patch<IProductReview>(`${this.resourceUrl}/${getProductReviewIdentifier(productReview) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductReview>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductReview[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductReviewToCollectionIfMissing(
    productReviewCollection: IProductReview[],
    ...productReviewsToCheck: (IProductReview | null | undefined)[]
  ): IProductReview[] {
    const productReviews: IProductReview[] = productReviewsToCheck.filter(isPresent);
    if (productReviews.length > 0) {
      const productReviewCollectionIdentifiers = productReviewCollection.map(
        productReviewItem => getProductReviewIdentifier(productReviewItem)!
      );
      const productReviewsToAdd = productReviews.filter(productReviewItem => {
        const productReviewIdentifier = getProductReviewIdentifier(productReviewItem);
        if (productReviewIdentifier == null || productReviewCollectionIdentifiers.includes(productReviewIdentifier)) {
          return false;
        }
        productReviewCollectionIdentifiers.push(productReviewIdentifier);
        return true;
      });
      return [...productReviewsToAdd, ...productReviewCollection];
    }
    return productReviewCollection;
  }

  protected convertDateFromClient(productReview: IProductReview): IProductReview {
    return Object.assign({}, productReview, {
      createdAt: productReview.createdAt?.isValid() ? productReview.createdAt.toJSON() : undefined,
      updatedAt: productReview.updatedAt?.isValid() ? productReview.updatedAt.toJSON() : undefined,
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
      res.body.forEach((productReview: IProductReview) => {
        productReview.createdAt = productReview.createdAt ? dayjs(productReview.createdAt) : undefined;
        productReview.updatedAt = productReview.updatedAt ? dayjs(productReview.updatedAt) : undefined;
      });
    }
    return res;
  }
}
