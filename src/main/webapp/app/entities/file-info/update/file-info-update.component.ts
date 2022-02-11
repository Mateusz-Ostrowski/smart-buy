import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFileInfo, FileInfo } from '../file-info.model';
import { FileInfoService } from '../service/file-info.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-file-info-update',
  templateUrl: './file-info-update.component.html',
})
export class FileInfoUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    fileName: [],
    originalFileName: [],
    fileSize: [],
    createdAt: [],
    updatedAt: [],
    imageOf: [],
  });

  constructor(
    protected fileInfoService: FileInfoService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileInfo }) => {
      if (fileInfo.id === undefined) {
        const today = dayjs().startOf('day');
        fileInfo.createdAt = today;
        fileInfo.updatedAt = today;
      }

      this.updateForm(fileInfo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileInfo = this.createFromForm();
    if (fileInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.fileInfoService.update(fileInfo));
    } else {
      this.subscribeToSaveResponse(this.fileInfoService.create(fileInfo));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileInfo>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fileInfo: IFileInfo): void {
    this.editForm.patchValue({
      id: fileInfo.id,
      fileName: fileInfo.fileName,
      originalFileName: fileInfo.originalFileName,
      fileSize: fileInfo.fileSize,
      createdAt: fileInfo.createdAt ? fileInfo.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: fileInfo.updatedAt ? fileInfo.updatedAt.format(DATE_TIME_FORMAT) : null,
      imageOf: fileInfo.imageOf,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, fileInfo.imageOf);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('imageOf')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): IFileInfo {
    return {
      ...new FileInfo(),
      id: this.editForm.get(['id'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      originalFileName: this.editForm.get(['originalFileName'])!.value,
      fileSize: this.editForm.get(['fileSize'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      imageOf: this.editForm.get(['imageOf'])!.value,
    };
  }
}
