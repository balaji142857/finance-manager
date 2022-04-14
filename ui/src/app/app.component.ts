import { Component, ViewChild } from '@angular/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Finance Manager';
  connectedUser = 'Balaji KRISHNAN'
  showFilter = false;
  @ViewChild('drawer') drawer;

  menuItems = [{ icon: 'dialpad', link: 'settings', title: 'Preferences' },
  { icon: 'dialpad', link: 'settings', title: 'Themes' },
  { icon: 'dialpad', link: 'settings', title: 'Categories' },
  { icon: 'dialpad', link: 'settings', title: 'Notifications' }];

  hamClicked() {
    this.drawer.toggle();
  }

}
