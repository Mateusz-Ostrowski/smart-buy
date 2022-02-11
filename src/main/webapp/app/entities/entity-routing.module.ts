import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        data: { pageTitle: 'Customers' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'Products' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'file-info',
        data: { pageTitle: 'FileInfos' },
        loadChildren: () => import('./file-info/file-info.module').then(m => m.FileInfoModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'product-review',
        data: { pageTitle: 'ProductReviews' },
        loadChildren: () => import('./product-review/product-review.module').then(m => m.ProductReviewModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'Addresses' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'cart',
        data: { pageTitle: 'Carts' },
        loadChildren: () => import('./cart/cart.module').then(m => m.CartModule),
      },
      {
        path: 'cart-item',
        data: { pageTitle: 'CartItems' },
        loadChildren: () => import('./cart-item/cart-item.module').then(m => m.CartItemModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'Orders' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'order-item',
        data: { pageTitle: 'OrderItems' },
        loadChildren: () => import('./order-item/order-item.module').then(m => m.OrderItemModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
