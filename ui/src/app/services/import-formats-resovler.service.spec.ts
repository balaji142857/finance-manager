import { TestBed } from '@angular/core/testing';

import { ImportFormatsResovlerService } from './import-formats-resovler.service';

describe('ImportFormatsResovlerService', () => {
  let service: ImportFormatsResovlerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImportFormatsResovlerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
