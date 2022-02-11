import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileInfoComponent } from '../list/file-info.component';
import { FileInfoDetailComponent } from '../detail/file-info-detail.component';
import { FileInfoUpdateComponent } from '../update/file-info-update.component';
import { FileInfoRoutingResolveService } from './file-info-routing-resolve.service';

const fileInfoRoute: Routes = [
  {
    path: '',
    component: FileInfoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileInfoDetailComponent,
    resolve: {
      fileInfo: FileInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileInfoUpdateComponent,
    resolve: {
      fileInfo: FileInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileInfoUpdateComponent,
    resolve: {
      fileInfo: FileInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fileInfoRoute)],
  exports: [RouterModule],
})
export class FileInfoRoutingModule {}
