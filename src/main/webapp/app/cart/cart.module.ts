import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../../../../../smartbuy2/src/main/webapp/app/shared/shared.module';
import { CART_ROUTE } from './cart.route';
import { CartComponent } from './cart.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([CART_ROUTE])],
  declarations: [CartComponent],
})
export class CartModule {}
