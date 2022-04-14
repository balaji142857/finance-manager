import { Component, OnInit, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';


@Component({
	selector: 'app-snackbar',
	templateUrl: './snackbar.component.html',
	styleUrls: ['./snackbar.component.scss']
})
export class SnackbarComponent {

	constructor(@Inject(MAT_SNACK_BAR_DATA) public data: any,
		private _snackRef: MatSnackBarRef<SnackbarComponent>,) { }


	close() {
		this._snackRef.dismiss();
	}

}
