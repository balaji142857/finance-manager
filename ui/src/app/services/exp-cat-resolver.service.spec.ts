import { TestBed } from '@angular/core/testing';

import { ExpCatResolverService } from './exp-cat-resolver.service';

describe('ExpCatResolverService', () => {
  let service: ExpCatResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpCatResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
