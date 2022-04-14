import { TestBed } from '@angular/core/testing';

import { ExpnseResolverService } from './expnse-resolver.service';

describe('ExpnseResolverService', () => {
  let service: ExpnseResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpnseResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
