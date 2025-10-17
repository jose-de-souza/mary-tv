import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { User } from '../../models';
import { UserService } from '../../services/user.service';
import { DataTableComponent } from '../data-table/data-table'; // Correct path
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog'; // Correct path
import { UserEditDialogComponent } from './user-edit-dialog'; // Correct path

@Component({
  selector: 'app-user-maintenance',
  standalone: true,
  imports: [
    CommonModule,
    DataTableComponent,
    MatDialogModule,
    MatSnackBarModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './user-maintenance.html',
  styleUrls: ['./user-maintenance.scss'],
})
export class UserMaintenanceComponent implements OnInit {
  users: User[] = [];
  columns: string[] = ['username', 'role', 'actions'];

  constructor(
    private userService: UserService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe((response) => {
      if (response.success) {
        this.users = response.data;
      }
    });
  }

  openUserEditDialog(user?: User): void {
    const dialogRef = this.dialog.open(UserEditDialogComponent, {
      width: '400px',
      data: { user },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadUsers();
      }
    });
  }

  deleteUser(user: User): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        message: `Are you sure you want to delete the user "${user.username}"?`,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.userService.deleteUser(user.id).subscribe({
          next: () => {
            this.snackBar.open('User deleted successfully', 'Close', {
              duration: 3000,
            });
            this.loadUsers();
          },
          error: (err) => {
            this.snackBar.open(
              err.error?.message || 'An error occurred while deleting the user',
              'Close',
              {
                duration: 3000,
              }
            );
          },
        });
      }
    });
  }
}