import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { RestService } from '../rest.service';
import { Observable } from 'rxjs';
import { ChartDataModel } from '../models/chart-data.model';

@Injectable({
  providedIn: 'root'
})
export class MonthlyExpenseResolverService implements Resolve<Observable<ChartDataModel>> {
  constructor(private service: RestService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot ): Observable<ChartDataModel>{
    return this.service.getMonthlyExpenses({});
  }
}
