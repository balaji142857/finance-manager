import { TestBed } from '@angular/core/testing';

import { MonthlyExpenseResolverService } from './monthly-expense-resolver.service';

describe('MonthlyExpenseResolverService', () => {
  let service: MonthlyExpenseResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonthlyExpenseResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
