import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-generic-dialog',
  templateUrl: './generic-dialog.component.html',
  styleUrls: ['./generic-dialog.component.scss']
})
export class GenericDialogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data,
  private dialogRef: MatDialogRef<GenericDialogComponent>) { }

  ngOnInit(): void {
  }

  cancel() { this.dialogRef.close(); }

  confirm() {
    this.dialogRef.close({
        confirmed: true
    });
  }

}
