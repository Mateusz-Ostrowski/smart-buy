import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ICategory } from 'app/entities/category/category.model';
import { HttpResponse } from '@angular/common/http';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { LabelType, Options } from '@angular-slider/ngx-slider';
import { CartService } from 'app/entities/cart/service/cart.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  imageUrl = 'http://localhost:8080/uploaded/';
  account: Account | null = null;
  categories?: ICategory[];
  products?: IProduct[];
  value: number;
  highValue: number;
  options: Options = {
    floor: 0,
    ceil: 100,
    translate: (value: number): string => {
      if (value === this.options.ceil) {
        return 'powyżej ' + value.toString() + ' zł';
      }
      return value.toString() + ' zł';
    },
  };

  private readonly destroy$ = new Subject<void>();

  constructor(
    private cartService: CartService,
    private accountService: AccountService,
    private router: Router,
    private categoryService: CategoryService,
    private productService: ProductService,
    private activatedRoute: ActivatedRoute
  ) {
    this.value = 0;
    this.highValue = 100;
  }
  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    this.categoryService.getPublicCategories({}).subscribe({
      next: (res: HttpResponse<ICategory[]>) => {
        this.categories = res.body ?? [];
      },
    });
    this.activatedRoute.params.subscribe(() => {
      this.productService.getPublicProducts(this.activatedRoute.snapshot.paramMap.get('id')).subscribe({
        next: (res: HttpResponse<ICategory[]>) => {
          this.products = res.body ?? [];
        },
      });
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  addToCart(productId: number | undefined): void {
    this.cartService.addItemToCart(productId, 1);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
