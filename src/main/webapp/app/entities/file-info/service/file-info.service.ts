import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFileInfo, getFileInfoIdentifier } from '../file-info.model';

export type EntityResponseType = HttpResponse<IFileInfo>;
export type EntityArrayResponseType = HttpResponse<IFileInfo[]>;

@Injectable({ providedIn: 'root' })
export class FileInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/file-infos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fileInfo: IFileInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileInfo);
    return this.http
      .post<IFileInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fileInfo: IFileInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileInfo);
    return this.http
      .put<IFileInfo>(`${this.resourceUrl}/${getFileInfoIdentifier(fileInfo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fileInfo: IFileInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileInfo);
    return this.http
      .patch<IFileInfo>(`${this.resourceUrl}/${getFileInfoIdentifier(fileInfo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFileInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFileInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFileInfoToCollectionIfMissing(fileInfoCollection: IFileInfo[], ...fileInfosToCheck: (IFileInfo | null | undefined)[]): IFileInfo[] {
    const fileInfos: IFileInfo[] = fileInfosToCheck.filter(isPresent);
    if (fileInfos.length > 0) {
      const fileInfoCollectionIdentifiers = fileInfoCollection.map(fileInfoItem => getFileInfoIdentifier(fileInfoItem)!);
      const fileInfosToAdd = fileInfos.filter(fileInfoItem => {
        const fileInfoIdentifier = getFileInfoIdentifier(fileInfoItem);
        if (fileInfoIdentifier == null || fileInfoCollectionIdentifiers.includes(fileInfoIdentifier)) {
          return false;
        }
        fileInfoCollectionIdentifiers.push(fileInfoIdentifier);
        return true;
      });
      return [...fileInfosToAdd, ...fileInfoCollection];
    }
    return fileInfoCollection;
  }

  protected convertDateFromClient(fileInfo: IFileInfo): IFileInfo {
    return Object.assign({}, fileInfo, {
      createdAt: fileInfo.createdAt?.isValid() ? fileInfo.createdAt.toJSON() : undefined,
      updatedAt: fileInfo.updatedAt?.isValid() ? fileInfo.updatedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fileInfo: IFileInfo) => {
        fileInfo.createdAt = fileInfo.createdAt ? dayjs(fileInfo.createdAt) : undefined;
        fileInfo.updatedAt = fileInfo.updatedAt ? dayjs(fileInfo.updatedAt) : undefined;
      });
    }
    return res;
  }
}
