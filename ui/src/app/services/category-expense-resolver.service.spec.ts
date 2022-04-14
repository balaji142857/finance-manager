import { TestBed } from '@angular/core/testing';

import { CategoryExpenseResolverService } from './category-expense-resolver.service';

describe('CategoryExpenseResolverService', () => {
  let service: CategoryExpenseResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoryExpenseResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
