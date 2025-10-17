import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { Item } from '../../models';
import { ItemService } from '../../services/item.service';
import { AuthService } from '../../services/auth.service';
import { DataTableComponent } from '../data-table/data-table';
import { VideoPlayerDialogComponent } from '../video-player-dialog/video-player-dialog'; // Correct import
import { ItemEditDialogComponent } from '../item-edit-dialog/item-edit-dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog';

@Component({
  selector: 'app-episodes-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    DataTableComponent,
    MatDialogModule,
    MatPaginatorModule,
    MatSnackBarModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './episodes-list.html',
  styleUrls: ['./episodes-list.scss'],
})
export class EpisodesListComponent implements OnInit {
  episodes: Item[] = [];
  columns: string[] = ['title', 'description', 'itemDate'];
  seriesId!: number;
  seriesTitle = '';

  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private itemService: ItemService,
    private route: ActivatedRoute,
    public authService: AuthService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.seriesId = +this.route.snapshot.paramMap.get('seriesId')!;
    if (this.authService.isAuthenticated()) {
      this.columns.push('actions');
    }
    this.loadEpisodes();
  }

  loadEpisodes(event?: PageEvent): void {
    const page = event ? event.pageIndex : this.pageIndex;
    const size = event ? event.pageSize : this.pageSize;

    this.itemService
      .getEpisodes(this.seriesId, page, size)
      .subscribe((res) => {
        if (res.success) {
          this.episodes = res.data.content;
          this.totalElements = res.data.totalElements;
          // Try to get the title - this relies on the backend sending parent info or requires another call
          if (this.episodes.length > 0) {
             // For now, use the ID until backend details are confirmed
            this.seriesTitle = `Series #${this.seriesId}`;
          }
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
        this.loadEpisodes();
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
            this.snackBar.open('Episode deleted successfully', 'Close', {
              duration: 3000,
            });
            this.loadEpisodes();
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