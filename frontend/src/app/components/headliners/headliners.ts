import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { Item } from '../../models';
import { ItemService } from '../../services/item.service';
import { AuthService } from '../../services/auth.service';
import { DataTableComponent } from '../data-table/data-table';
import { VideoPlayerDialogComponent } from '../video-player-dialog/video-player-dialog'; // Correct path
import { ItemEditDialogComponent } from '../item-edit-dialog/item-edit-dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog';

@Component({
  selector: 'app-headliners',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    DataTableComponent,
    MatPaginatorModule,
    MatDialogModule,
    MatSnackBarModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './headliners.html',
  styleUrls: ['./headliners.scss'],
})
export class HeadlinersComponent implements OnInit {
  headliners: Item[] = [];
  columns: string[] = ['title', 'description', 'itemDate'];
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private itemService: ItemService,
    public authService: AuthService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.columns.push('actions');
    }
    this.loadHeadliners();
  }

  loadHeadliners(event?: PageEvent): void {
    const page = event ? event.pageIndex : this.pageIndex;
    const size = event ? event.pageSize : this.pageSize;

    this.itemService.getHeadliners(page, size).subscribe((res) => {
      if (res.success) {
        this.headliners = res.data.content;
        this.totalElements = res.data.totalElements;
      }
    });
  }

  playVideo(item: Item): void {
    this.dialog.open(VideoPlayerDialogComponent, {
      width: '80vw',
      data: { title: item.title, videoUrl: item.videoUrl },
    });
  }

  openItemEditDialog(item: Item): void {
    const dialogRef = this.dialog.open(ItemEditDialogComponent, {
      width: '600px',
      data: { item },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadHeadliners();
      }
    });
  }

  deleteItem(item: Item): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: `Are you sure you want to delete "${item.title}"?` },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.itemService.deleteItem(item.id).subscribe({
          next: () => {
            this.snackBar.open('Item deleted successfully', 'Close', {
              duration: 3000,
            });
            this.loadHeadliners();
          },
          error: (err) => {
            this.snackBar.open(
              err.error?.message || 'An unknown error occurred',
              'Close',
              { duration: 3000 }
            );
          },
        });
      }
    });
  }
}