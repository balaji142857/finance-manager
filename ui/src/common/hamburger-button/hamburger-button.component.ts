import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-hamburger-button',
  templateUrl: './hamburger-button.component.html',
  styleUrls: ['./hamburger-button.component.scss']
})
export class HamburgerButtonComponent implements OnInit {

  hamburgerClass = false;
  @Output() hamClicked = new EventEmitter();

  constructor() { }


  ngOnInit(): void {
  }

  emitClickEvent($event) {
    this.hamburgerClass = !this.hamburgerClass;
    this.hamClicked.emit();
    $event.stopPropagation();
    $event.preventDefault();
  }

}
