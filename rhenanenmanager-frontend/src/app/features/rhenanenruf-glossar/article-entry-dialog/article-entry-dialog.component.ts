import { Component, inject, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { ArticleEntry } from '../../../core/models/article-entry.model';

export interface ArticleEntryDialogData {
  article?: ArticleEntry;
  categories: string[];
  mode: 'create' | 'edit';
}

@Component({
  selector: 'app-article-entry-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './article-entry-dialog.component.html',
  styleUrl: './article-entry-dialog.component.scss'
})
export class ArticleEntryDialogComponent implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<ArticleEntryDialogComponent>);

  articleForm!: FormGroup;
  title: string;
  categories: string[];

  constructor(@Inject(MAT_DIALOG_DATA) public data: ArticleEntryDialogData) {
    this.title = data.mode === 'create' ? 'Neuen Artikel erstellen' : 'Artikel bearbeiten';
    this.categories = data.categories;
  }

  ngOnInit(): void {
    this.articleForm = this.fb.group({
      title: [this.data.article?.title || '', [Validators.required]],
      subtitle: [this.data.article?.subtitle || ''],
      alternativeAuthor: [this.data.article?.alternativeAuthor || ''],
      category: [this.data.article?.category || ''],
      text: [this.data.article?.text || ''],
      year: [this.data.article?.year || new Date().getFullYear(), [Validators.min(1900), Validators.max(2100)]],
      month: [this.data.article?.month || null, [Validators.min(1), Validators.max(12)]],
      page: [this.data.article?.page || null],
      date: [this.data.article?.date || null]
    });
  }

  onSubmit(): void {
    if (this.articleForm.valid) {
      const result = {
        ...this.articleForm.value,
        id: this.data.article?.id
      };
      this.dialogRef.close(result);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
