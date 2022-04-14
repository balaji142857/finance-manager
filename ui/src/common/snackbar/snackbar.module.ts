import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SnackbarComponent } from './snackbar.component';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [
	CommonModule,
	MatIconModule
  ],
  declarations: [SnackbarComponent],
  entryComponents: [SnackbarComponent],
  exports : [SnackbarComponent]
})
export class SnackbarModule { }
