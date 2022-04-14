import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { RestService } from '../rest.service';
import { Observable } from 'rxjs';
import { ExpenseModel } from '../models/expense.model';

@Injectable({
  providedIn: 'root'
})
export class ExpenseResolverService implements Resolve<Observable<ExpenseModel[]>>{

  constructor(private service: RestService) { }

  resolve(
    route: ActivatedRouteSnapshot, state: RouterStateSnapshot ): Observable<any>|Promise<any>|any {
      return this.service.listExpenses();
  }
}
