import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICartItem, CartItem } from '../cart-item.model';
import { CartItemService } from '../service/cart-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICart } from 'app/entities/cart/cart.model';
import { CartService } from 'app/entities/cart/service/cart.service';

@Component({
  selector: 'jhi-cart-item-update',
  templateUrl: './cart-item-update.component.html',
})
export class CartItemUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];
  cartsSharedCollection: ICart[] = [];

  editForm = this.fb.group({
    id: [],
    price: [],
    quantity: [],
    createdAt: [],
    updatedAt: [],
    product: [],
    cart: [],
  });

  constructor(
    protected cartItemService: CartItemService,
    protected productService: ProductService,
    protected cartService: CartService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cartItem }) => {
      if (cartItem.id === undefined) {
        const today = dayjs().startOf('day');
        cartItem.createdAt = today;
        cartItem.updatedAt = today;
      }

      this.updateForm(cartItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cartItem = this.createFromForm();
    if (cartItem.id !== undefined) {
      this.subscribeToSaveResponse(this.cartItemService.update(cartItem));
    } else {
      this.subscribeToSaveResponse(this.cartItemService.create(cartItem));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackCartById(index: number, item: ICart): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICartItem>>): void {
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

  protected updateForm(cartItem: ICartItem): void {
    this.editForm.patchValue({
      id: cartItem.id,
      price: cartItem.price,
      quantity: cartItem.quantity,
      createdAt: cartItem.createdAt ? cartItem.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: cartItem.updatedAt ? cartItem.updatedAt.format(DATE_TIME_FORMAT) : null,
      product: cartItem.product,
      cart: cartItem.cart,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, cartItem.product);
    this.cartsSharedCollection = this.cartService.addCartToCollectionIfMissing(this.cartsSharedCollection, cartItem.cart);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.cartService
      .query()
      .pipe(map((res: HttpResponse<ICart[]>) => res.body ?? []))
      .pipe(map((carts: ICart[]) => this.cartService.addCartToCollectionIfMissing(carts, this.editForm.get('cart')!.value)))
      .subscribe((carts: ICart[]) => (this.cartsSharedCollection = carts));
  }

  protected createFromForm(): ICartItem {
    return {
      ...new CartItem(),
      id: this.editForm.get(['id'])!.value,
      price: this.editForm.get(['price'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      product: this.editForm.get(['product'])!.value,
      cart: this.editForm.get(['cart'])!.value,
    };
  }
}
