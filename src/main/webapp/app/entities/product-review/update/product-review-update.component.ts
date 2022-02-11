import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProductReview, ProductReview } from '../product-review.model';
import { ProductReviewService } from '../service/product-review.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

@Component({
  selector: 'jhi-product-review-update',
  templateUrl: './product-review-update.component.html',
})
export class ProductReviewUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];
  customersSharedCollection: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    text: [],
    rating: [],
    createdAt: [],
    updatedAt: [],
    product: [],
    customer: [],
  });

  constructor(
    protected productReviewService: ProductReviewService,
    protected productService: ProductService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productReview }) => {
      if (productReview.id === undefined) {
        const today = dayjs().startOf('day');
        productReview.createdAt = today;
        productReview.updatedAt = today;
      }

      this.updateForm(productReview);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productReview = this.createFromForm();
    if (productReview.id !== undefined) {
      this.subscribeToSaveResponse(this.productReviewService.update(productReview));
    } else {
      this.subscribeToSaveResponse(this.productReviewService.create(productReview));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackCustomerById(index: number, item: ICustomer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductReview>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productReview: IProductReview): void {
    this.editForm.patchValue({
      id: productReview.id,
      text: productReview.text,
      rating: productReview.rating,
      createdAt: productReview.createdAt ? productReview.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: productReview.updatedAt ? productReview.updatedAt.format(DATE_TIME_FORMAT) : null,
      product: productReview.product,
      customer: productReview.customer,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      productReview.product
    );
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing(
      this.customersSharedCollection,
      productReview.customer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing(customers, this.editForm.get('customer')!.value)
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }

  protected createFromForm(): IProductReview {
    return {
      ...new ProductReview(),
      id: this.editForm.get(['id'])!.value,
      text: this.editForm.get(['text'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      product: this.editForm.get(['product'])!.value,
      customer: this.editForm.get(['customer'])!.value,
    };
  }
}
