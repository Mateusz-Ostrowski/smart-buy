import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ProductStatus } from 'app/entities/enumerations/product-status.model';
import { NgxFileDropEntry } from 'ngx-file-drop';
import { FileInfo } from '../../file-info/file-info.model';
import { FileInfoService } from '../../file-info/service/file-info.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
  styleUrls: ['./product-update.component.scss'],
})
export class ProductUpdateComponent implements OnInit {
  fileDropMessage = 'Drag and drop files or ';
  filesDuringDrop = false;
  fileIds: number[] = [];
  isSaving = false;
  productStatusValues = Object.keys(ProductStatus);
  categoriesSharedCollection: ICategory[] = [];
  public draggedFiles: NgxFileDropEntry[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    price: [],
    quantity: [],
    discountPercent: [],
    status: [],
    createdAt: [],
    updatedAt: [],
    category: [],
  });

  constructor(
    protected productService: ProductService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected fileInfoService: FileInfoService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      if (product.id === undefined) {
        const today = dayjs().startOf('day');
        product.createdAt = today;
        product.updatedAt = today;
      }

      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  public dropped(files: NgxFileDropEntry[]): void {
    this.filesDuringDrop = false;
    this.fileDropMessage = 'Files have been dropped successfully you can now submit form or drag and drop other files';
    this.draggedFiles = files;

    for (const droppedFile of files) {
      if (droppedFile.fileEntry.isFile) {
        const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
        fileEntry.file((file: File) => {
          this.fileInfoService.upload(file).subscribe(value => {
            if (value.id) {
              this.fileIds.push(value.id);
            }
          });
        });
      }
    }
  }

  public fileOver(event: any): void {
    this.fileDropMessage = 'Drop files';
    this.filesDuringDrop = true;
  }

  public fileLeave(event: any): void {
    this.fileDropMessage = 'Drag files or ';
    this.filesDuringDrop = false;
  }
  public removeFileFromUploads(index: number): void {
    delete this.draggedFiles[index];
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      name: product.name,
      price: product.price,
      quantity: product.quantity,
      discountPercent: product.discountPrice,
      status: product.status,
      createdAt: product.createdAt ? product.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: product.updatedAt ? product.updatedAt.format(DATE_TIME_FORMAT) : null,
      category: product.category,
    });

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      product.category
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      price: this.editForm.get(['price'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      discountPrice: this.editForm.get(['discountPercent'])!.value,
      status: this.editForm.get(['status'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      category: this.editForm.get(['category'])!.value,
      fileIds: this.fileIds,
    };
  }
}
