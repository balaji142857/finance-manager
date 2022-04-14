import { Component, Input } from '@angular/core';
import { NavModel } from './nav.model';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent {

  @Input() items: NavModel[] =[{
    label: 'Dashboard',
    route: 'dashboard',
    icon: 'assessment'
  }, {
    label: 'Assets',
    route: 'assets',
    icon: 'account_balance'
  }, {
    label: 'Expense',
    route: 'expense',
    icon: 'account_balance_wallet'
  }, {
    label: 'Bulk Import',
    route: 'reports',
    icon: 'description'
  }, {
    label: 'Settings',
    route: 'settings',
    icon: 'settings'
  }];

  constructor() { }

}
