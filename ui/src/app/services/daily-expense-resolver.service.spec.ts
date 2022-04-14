import { TestBed } from '@angular/core/testing';

import { DailyExpenseResolverService } from './daily-expense-resolver.service';

describe('DailyExpenseResolverService', () => {
  let service: DailyExpenseResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DailyExpenseResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
