import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { RestService } from '../rest.service';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ImportFormatsResovlerService implements Resolve<Observable<String[]>> {
  
  constructor(private service: RestService) {}

  resolve(
    route: ActivatedRouteSnapshot, state: RouterStateSnapshot ): Observable<any>|Promise<any>|any {
    return this.service.getImportFormats();
  }
}
