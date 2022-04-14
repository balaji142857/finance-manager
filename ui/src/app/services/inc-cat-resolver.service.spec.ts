import { TestBed } from '@angular/core/testing';

import { IncCatResolverService } from './inc-cat-resolver.service';

describe('IncCatResolverService', () => {
  let service: IncCatResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IncCatResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
