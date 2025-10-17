import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { Item, Category } from '../../models';
import { ItemService } from '../../services/item.service';
import { AuthService } from '../../services/auth.service';
import { CategoryService } from '../../services/category.service';
import { DataTableComponent } from '../data-table/data-table';
import { ItemEditDialogComponent } from '../item-edit-dialog/item-edit-dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog';
import { VideoPlayerDialogComponent } from '../video-player-dialog/video-player-dialog'; // Correct path

@Component({
  selector: 'app-items-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    DataTableComponent,
    MatDialogModule,
    MatSnackBarModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './items-list.html',
  styleUrls: ['./items-list.scss'],
})
export class ItemsListComponent implements OnInit {
  items: Item[] = [];
  columns: string[] = ['title', 'description', 'contentType', 'itemDate'];
  categories: Category[] = [];
  filterForm!: FormGroup;
  hasHeadliners = false;

  constructor(
    private itemService: ItemService,
    private categoryService: CategoryService,
    public authService: AuthService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.columns.push('actions');
    }
    this.filterForm = this.fb.group({
      keyword: [null],
      categoryId: [null],
      isHeadline: [null],
      fromDate: [null],
      toDate: [null],
    });
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.loadItems();
    this.categoryService.getAllCategories().subscribe((res) => {
      if (res.success) {
        this.categories = res.data;
      }
    });
    this.itemService.getHeadliners(0, 1).subscribe((res) => {
      if (res.success && res.data.totalElements > 0) {
        this.hasHeadliners = true;
      }
    });
  }

  loadItems(): void {
    const filters = this.filterForm.value;
    if (filters.fromDate) {
      filters.fromDate = new Date(filters.fromDate).toISOString();
    }
    if (filters.toDate) {
      filters.toDate = new Date(filters.toDate).toISOString();
    }
    this.itemService.getShowsFiltered(filters).subscribe((response) => {
      if (response.success) {
        this.items = response.data.content;
      }
    });
  }

  applyFilters(): void {
    this.loadItems();
  }

  resetFilters(): void {
    this.filterForm.reset();
    this.loadItems();
  }

  onRowClicked(item: Item): void {
    if (item.contentType === 'SERIES_HEADER') {
      this.router.navigate(['/series', item.id, 'episodes']);
    } else {
      this.dialog.open(VideoPlayerDialogComponent, {
        width: '80vw',
        data: { title: item.title, videoUrl: item.videoUrl },
      });
    }
  }

  openItemEditDialog(item?: Item): void {
    const dialogRef = this.dialog.open(ItemEditDialogComponent, {
      width: '600px',
      data: { item },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadItems();
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
            this.loadItems();
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