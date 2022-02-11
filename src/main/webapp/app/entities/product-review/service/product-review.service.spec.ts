import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductReview, ProductReview } from '../product-review.model';

import { ProductReviewService } from './product-review.service';

describe('ProductReview Service', () => {
  let service: ProductReviewService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductReview;
  let expectedResult: IProductReview | IProductReview[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductReviewService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      text: 'AAAAAAA',
      rating: 0,
      createdAt: currentDate,
      updatedAt: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          updatedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ProductReview', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          updatedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          updatedAt: currentDate,
        },
        returnedFromService
      );

      service.create(new ProductReview()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductReview', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          text: 'BBBBBB',
          rating: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          updatedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          updatedAt: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductReview', () => {
      const patchObject = Object.assign(
        {
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        new ProductReview()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdAt: currentDate,
          updatedAt: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductReview', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          text: 'BBBBBB',
          rating: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          updatedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          updatedAt: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ProductReview', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProductReviewToCollectionIfMissing', () => {
      it('should add a ProductReview to an empty array', () => {
        const productReview: IProductReview = { id: 123 };
        expectedResult = service.addProductReviewToCollectionIfMissing([], productReview);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productReview);
      });

      it('should not add a ProductReview to an array that contains it', () => {
        const productReview: IProductReview = { id: 123 };
        const productReviewCollection: IProductReview[] = [
          {
            ...productReview,
          },
          { id: 456 },
        ];
        expectedResult = service.addProductReviewToCollectionIfMissing(productReviewCollection, productReview);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductReview to an array that doesn't contain it", () => {
        const productReview: IProductReview = { id: 123 };
        const productReviewCollection: IProductReview[] = [{ id: 456 }];
        expectedResult = service.addProductReviewToCollectionIfMissing(productReviewCollection, productReview);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productReview);
      });

      it('should add only unique ProductReview to an array', () => {
        const productReviewArray: IProductReview[] = [{ id: 123 }, { id: 456 }, { id: 18215 }];
        const productReviewCollection: IProductReview[] = [{ id: 123 }];
        expectedResult = service.addProductReviewToCollectionIfMissing(productReviewCollection, ...productReviewArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productReview: IProductReview = { id: 123 };
        const productReview2: IProductReview = { id: 456 };
        expectedResult = service.addProductReviewToCollectionIfMissing([], productReview, productReview2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productReview);
        expect(expectedResult).toContain(productReview2);
      });

      it('should accept null and undefined values', () => {
        const productReview: IProductReview = { id: 123 };
        expectedResult = service.addProductReviewToCollectionIfMissing([], null, productReview, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productReview);
      });

      it('should return initial array if no ProductReview is added', () => {
        const productReviewCollection: IProductReview[] = [{ id: 123 }];
        expectedResult = service.addProductReviewToCollectionIfMissing(productReviewCollection, undefined, null);
        expect(expectedResult).toEqual(productReviewCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
