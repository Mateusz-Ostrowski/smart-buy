import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFileInfo, FileInfo } from '../file-info.model';

import { FileInfoService } from './file-info.service';

describe('FileInfo Service', () => {
  let service: FileInfoService;
  let httpMock: HttpTestingController;
  let elemDefault: IFileInfo;
  let expectedResult: IFileInfo | IFileInfo[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FileInfoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fileName: 'AAAAAAA',
      originalFileName: 'AAAAAAA',
      fileSize: 0,
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

    it('should create a FileInfo', () => {
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

      service.create(new FileInfo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fileName: 'BBBBBB',
          originalFileName: 'BBBBBB',
          fileSize: 1,
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

    it('should partial update a FileInfo', () => {
      const patchObject = Object.assign(
        {
          fileName: 'BBBBBB',
          originalFileName: 'BBBBBB',
          updatedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        new FileInfo()
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

    it('should return a list of FileInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fileName: 'BBBBBB',
          originalFileName: 'BBBBBB',
          fileSize: 1,
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

    it('should delete a FileInfo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFileInfoToCollectionIfMissing', () => {
      it('should add a FileInfo to an empty array', () => {
        const fileInfo: IFileInfo = { id: 123 };
        expectedResult = service.addFileInfoToCollectionIfMissing([], fileInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileInfo);
      });

      it('should not add a FileInfo to an array that contains it', () => {
        const fileInfo: IFileInfo = { id: 123 };
        const fileInfoCollection: IFileInfo[] = [
          {
            ...fileInfo,
          },
          { id: 456 },
        ];
        expectedResult = service.addFileInfoToCollectionIfMissing(fileInfoCollection, fileInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileInfo to an array that doesn't contain it", () => {
        const fileInfo: IFileInfo = { id: 123 };
        const fileInfoCollection: IFileInfo[] = [{ id: 456 }];
        expectedResult = service.addFileInfoToCollectionIfMissing(fileInfoCollection, fileInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileInfo);
      });

      it('should add only unique FileInfo to an array', () => {
        const fileInfoArray: IFileInfo[] = [{ id: 123 }, { id: 456 }, { id: 75876 }];
        const fileInfoCollection: IFileInfo[] = [{ id: 123 }];
        expectedResult = service.addFileInfoToCollectionIfMissing(fileInfoCollection, ...fileInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileInfo: IFileInfo = { id: 123 };
        const fileInfo2: IFileInfo = { id: 456 };
        expectedResult = service.addFileInfoToCollectionIfMissing([], fileInfo, fileInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileInfo);
        expect(expectedResult).toContain(fileInfo2);
      });

      it('should accept null and undefined values', () => {
        const fileInfo: IFileInfo = { id: 123 };
        expectedResult = service.addFileInfoToCollectionIfMissing([], null, fileInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileInfo);
      });

      it('should return initial array if no FileInfo is added', () => {
        const fileInfoCollection: IFileInfo[] = [{ id: 123 }];
        expectedResult = service.addFileInfoToCollectionIfMissing(fileInfoCollection, undefined, null);
        expect(expectedResult).toEqual(fileInfoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
