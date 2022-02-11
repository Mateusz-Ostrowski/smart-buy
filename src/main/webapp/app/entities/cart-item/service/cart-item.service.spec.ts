import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICartItem, CartItem } from '../cart-item.model';

import { CartItemService } from './cart-item.service';

describe('CartItem Service', () => {
  let service: CartItemService;
  let httpMock: HttpTestingController;
  let elemDefault: ICartItem;
  let expectedResult: ICartItem | ICartItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CartItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      price: 0,
      quantity: 0,
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

    it('should create a CartItem', () => {
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

      service.create(new CartItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CartItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          price: 1,
          quantity: 1,
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

    it('should partial update a CartItem', () => {
      const patchObject = Object.assign(
        {
          price: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        new CartItem()
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

    it('should return a list of CartItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          price: 1,
          quantity: 1,
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

    it('should delete a CartItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCartItemToCollectionIfMissing', () => {
      it('should add a CartItem to an empty array', () => {
        const cartItem: ICartItem = { id: 123 };
        expectedResult = service.addCartItemToCollectionIfMissing([], cartItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cartItem);
      });

      it('should not add a CartItem to an array that contains it', () => {
        const cartItem: ICartItem = { id: 123 };
        const cartItemCollection: ICartItem[] = [
          {
            ...cartItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addCartItemToCollectionIfMissing(cartItemCollection, cartItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CartItem to an array that doesn't contain it", () => {
        const cartItem: ICartItem = { id: 123 };
        const cartItemCollection: ICartItem[] = [{ id: 456 }];
        expectedResult = service.addCartItemToCollectionIfMissing(cartItemCollection, cartItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cartItem);
      });

      it('should add only unique CartItem to an array', () => {
        const cartItemArray: ICartItem[] = [{ id: 123 }, { id: 456 }, { id: 45279 }];
        const cartItemCollection: ICartItem[] = [{ id: 123 }];
        expectedResult = service.addCartItemToCollectionIfMissing(cartItemCollection, ...cartItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cartItem: ICartItem = { id: 123 };
        const cartItem2: ICartItem = { id: 456 };
        expectedResult = service.addCartItemToCollectionIfMissing([], cartItem, cartItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cartItem);
        expect(expectedResult).toContain(cartItem2);
      });

      it('should accept null and undefined values', () => {
        const cartItem: ICartItem = { id: 123 };
        expectedResult = service.addCartItemToCollectionIfMissing([], null, cartItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cartItem);
      });

      it('should return initial array if no CartItem is added', () => {
        const cartItemCollection: ICartItem[] = [{ id: 123 }];
        expectedResult = service.addCartItemToCollectionIfMissing(cartItemCollection, undefined, null);
        expect(expectedResult).toEqual(cartItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
