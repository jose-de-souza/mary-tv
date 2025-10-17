import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogModule,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { SafeUrlPipe } from '../../safe-url.pipe'; // Ensure this pipe exists at src/app/safe-url.pipe.ts

@Component({
  selector: 'app-video-player-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, SafeUrlPipe],
  templateUrl: './video-player-dialog.html',
  styleUrls: ['./video-player-dialog.scss'],
})
export class VideoPlayerDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<VideoPlayerDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { title: string; videoUrl: string }
  ) {}
}