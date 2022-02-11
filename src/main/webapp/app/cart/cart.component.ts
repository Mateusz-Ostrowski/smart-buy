import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import {ICartItem} from "../entities/cart-item/cart-item.model";
import {CartService} from "../entities/cart/service/cart.service";
import {HttpResponse} from "@angular/common/http";
import {ICategory} from "../entities/category/category.model";

@Component({
  selector: 'jhi-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  cartItems?: ICartItem[];
  private readonly destroy$ = new Subject<void>();


  constructor(private accountService: AccountService, private router: Router, private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.getCurrentUserCartItems()
      .subscribe({
        next: (res: HttpResponse<ICategory[]>) => {
          this.cartItems = res.body ?? [];
        }
      });
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
