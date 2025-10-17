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
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';

import { User, UserUpsert } from '../../models';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-edit-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule,
  ],
  templateUrl: './user-edit-dialog.html',
  styleUrls: ['./user-edit-dialog.scss'],
})
export class UserEditDialogComponent implements OnInit {
  userForm!: FormGroup;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<UserEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { user: User }
  ) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      username: [this.data.user?.username || '', Validators.required],
      password: [''],
      role: [this.data.user?.role || 'USER', Validators.required],
    });

    if (!this.data.user) {
      this.userForm.get('password')?.setValidators(Validators.required);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.userForm.invalid) {
      return;
    }

    const userUpsert: UserUpsert = this.userForm.value;
    if (!userUpsert.password) {
      delete userUpsert.password;
    }

    const saveObservable = this.data.user
      ? this.userService.updateUser(this.data.user.id, userUpsert)
      : this.userService.createUser(userUpsert);

    saveObservable.subscribe({
      next: () => {
        this.snackBar.open('User saved successfully', 'Close', {
          duration: 3000,
        });
        this.dialogRef.close(true);
      },
      error: (err) => {
        this.snackBar.open(
          err.error?.message || 'An error occurred while saving the user',
          'Close',
          { duration: 3000 }
        );
      },
    });
  }
}