import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductComponent } from './list/product.component';
import { ProductDetailComponent } from './detail/product-detail.component';
import { ProductUpdateComponent } from './update/product-update.component';
import { ProductDeleteDialogComponent } from './delete/product-delete-dialog.component';
import { ProductRoutingModule } from './route/product-routing.module';
import { NgxFileDropModule } from 'ngx-file-drop';

@NgModule({
  imports: [SharedModule, ProductRoutingModule, NgxFileDropModule],
  declarations: [ProductComponent, ProductDetailComponent, ProductUpdateComponent, ProductDeleteDialogComponent],
  entryComponents: [ProductDeleteDialogComponent],
})
export class ProductModule {}
