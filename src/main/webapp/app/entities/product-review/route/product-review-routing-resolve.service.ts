import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductReview, ProductReview } from '../product-review.model';
import { ProductReviewService } from '../service/product-review.service';

@Injectable({ providedIn: 'root' })
export class ProductReviewRoutingResolveService implements Resolve<IProductReview> {
  constructor(protected service: ProductReviewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductReview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productReview: HttpResponse<ProductReview>) => {
          if (productReview.body) {
            return of(productReview.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductReview());
  }
}
