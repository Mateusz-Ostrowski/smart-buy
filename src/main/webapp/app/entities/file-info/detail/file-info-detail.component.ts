import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFileInfo } from '../file-info.model';

@Component({
  selector: 'jhi-file-info-detail',
  templateUrl: './file-info-detail.component.html',
})
export class FileInfoDetailComponent implements OnInit {
  fileInfo: IFileInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileInfo }) => {
      this.fileInfo = fileInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
