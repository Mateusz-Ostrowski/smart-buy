import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FileInfoComponent } from './list/file-info.component';
import { FileInfoDetailComponent } from './detail/file-info-detail.component';
import { FileInfoUpdateComponent } from './update/file-info-update.component';
import { FileInfoDeleteDialogComponent } from './delete/file-info-delete-dialog.component';
import { FileInfoRoutingModule } from './route/file-info-routing.module';

@NgModule({
  imports: [SharedModule, FileInfoRoutingModule],
  declarations: [FileInfoComponent, FileInfoDetailComponent, FileInfoUpdateComponent, FileInfoDeleteDialogComponent],
  entryComponents: [FileInfoDeleteDialogComponent],
})
export class FileInfoModule {}
