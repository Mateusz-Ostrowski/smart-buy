import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileInfo, FileInfo } from '../file-info.model';
import { FileInfoService } from '../service/file-info.service';

@Injectable({ providedIn: 'root' })
export class FileInfoRoutingResolveService implements Resolve<IFileInfo> {
  constructor(protected service: FileInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fileInfo: HttpResponse<FileInfo>) => {
          if (fileInfo.body) {
            return of(fileInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FileInfo());
  }
}
