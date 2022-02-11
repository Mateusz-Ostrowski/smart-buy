import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICartItem } from '../cart-item.model';
import { CartItemService } from '../service/cart-item.service';

@Component({
  templateUrl: './cart-item-delete-dialog.component.html',
})
export class CartItemDeleteDialogComponent {
  cartItem?: ICartItem;

  constructor(protected cartItemService: CartItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cartItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
