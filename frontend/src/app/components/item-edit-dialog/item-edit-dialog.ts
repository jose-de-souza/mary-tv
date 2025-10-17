import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogModule,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Item, ItemUpsert } from '../../models';
import { ItemService } from '../../services/item.service';

@Component({
  selector: 'app-item-edit-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './item-edit-dialog.html',
  styleUrls: ['./item-edit-dialog.scss'],
})
export class ItemEditDialogComponent implements OnInit {
  itemForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private itemService: ItemService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<ItemEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { item: Item }
  ) {}

  ngOnInit(): void {
    this.itemForm = this.fb.group({
      title: [this.data.item?.title || '', Validators.required],
      description: [this.data.item?.description || ''],
      contentType: [this.data.item?.contentType || '', Validators.required],
      iconUrl: [this.data.item?.iconUrl || ''],
      videoUrl: [this.data.item?.videoUrl || '', Validators.required],
      itemDate: [this.data.item?.itemDate || new Date(), Validators.required],
      isNew: [this.data.item?.isNew || false],
      isHeadline: [this.data.item?.isHeadline || false],
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.itemForm.invalid) {
      return;
    }

    const itemUpsert: ItemUpsert = this.itemForm.value;

    const saveObservable = this.data.item
      ? this.itemService.updateItem(this.data.item.id, itemUpsert)
      : this.itemService.createItem(itemUpsert);

    saveObservable.subscribe({
      next: () => {
        this.snackBar.open('Item saved successfully', 'Close', {
          duration: 3000,
        });
        this.dialogRef.close(true);
      },
      error: (err) => {
        this.snackBar.open(
          err.error?.message || 'An error occurred while saving the item',
          'Close',
          { duration: 3000 }
        );
      },
    });
  }
}