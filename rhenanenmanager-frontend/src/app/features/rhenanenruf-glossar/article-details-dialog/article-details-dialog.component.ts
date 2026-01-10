import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { ArticleEntry, getFormattedIssue } from '../../../core/models/article-entry.model';

@Component({
  selector: 'app-article-details-dialog',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule
  ],
  templateUrl: './article-details-dialog.component.html',
  styleUrl: './article-details-dialog.component.scss'
})
export class ArticleDetailsDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ArticleDetailsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public article: ArticleEntry
  ) {}

  getFormattedIssue(): string {
    return getFormattedIssue(this.article);
  }

  formatDate(date: Date | string | null): string {
    if (!date) return 'Nicht angegeben';
    const d = new Date(date);
    return d.toLocaleDateString('de-DE', {
      day: '2-digit',
      month: 'long',
      year: 'numeric'
    });
  }

  onClose(): void {
    this.dialogRef.close();
  }
}
