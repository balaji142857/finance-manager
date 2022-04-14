import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { RestService } from '../rest.service';
import { Observable } from 'rxjs';
import { CategoryModel } from '../models/category.model';

@Injectable({
  providedIn: 'root'
})
export class ExpCatResolverService implements Resolve<Observable<CategoryModel[]>> {
  constructor(private service: RestService) {}

  resolve(
    route: ActivatedRouteSnapshot, state: RouterStateSnapshot ): Observable<any>|Promise<any>|any {
    return this.service.getExpenseCategories();
  }
}
