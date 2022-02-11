import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileInfo } from '../file-info.model';
import { FileInfoService } from '../service/file-info.service';

@Component({
  templateUrl: './file-info-delete-dialog.component.html',
})
export class FileInfoDeleteDialogComponent {
  fileInfo?: IFileInfo;

  constructor(protected fileInfoService: FileInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileInfoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
