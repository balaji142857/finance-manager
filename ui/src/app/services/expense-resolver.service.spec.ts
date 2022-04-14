import { TestBed } from '@angular/core/testing';

import { ExpenseResolverService } from './expense-resolver.service';

describe('ExpenseResolverService', () => {
  let service: ExpenseResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
