import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FileInfoDetailComponent } from './file-info-detail.component';

describe('FileInfo Management Detail Component', () => {
  let comp: FileInfoDetailComponent;
  let fixture: ComponentFixture<FileInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FileInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fileInfo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FileInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FileInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fileInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fileInfo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
