import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FileInfoService } from '../service/file-info.service';
import { IFileInfo, FileInfo } from '../file-info.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { FileInfoUpdateComponent } from './file-info-update.component';

describe('FileInfo Management Update Component', () => {
  let comp: FileInfoUpdateComponent;
  let fixture: ComponentFixture<FileInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fileInfoService: FileInfoService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FileInfoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FileInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileInfoService = TestBed.inject(FileInfoService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const fileInfo: IFileInfo = { id: 456 };
      const imageOf: IProduct = { id: 77983 };
      fileInfo.imageOf = imageOf;

      const productCollection: IProduct[] = [{ id: 32216 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [imageOf];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fileInfo });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fileInfo: IFileInfo = { id: 456 };
      const imageOf: IProduct = { id: 9085 };
      fileInfo.imageOf = imageOf;

      activatedRoute.data = of({ fileInfo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fileInfo));
      expect(comp.productsSharedCollection).toContain(imageOf);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileInfo>>();
      const fileInfo = { id: 123 };
      jest.spyOn(fileInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileInfo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileInfoService.update).toHaveBeenCalledWith(fileInfo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileInfo>>();
      const fileInfo = new FileInfo();
      jest.spyOn(fileInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileInfo }));
      saveSubject.complete();

      // THEN
      expect(fileInfoService.create).toHaveBeenCalledWith(fileInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileInfo>>();
      const fileInfo = { id: 123 };
      jest.spyOn(fileInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileInfoService.update).toHaveBeenCalledWith(fileInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
