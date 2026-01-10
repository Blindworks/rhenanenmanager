import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ArticleEntryService } from '../../../core/services/article-entry.service';
import { ThemeService } from '../../../core/services/theme.service';
import { AuthService } from '../../../core/services/auth.service';
import { ArticleEntry, getFormattedIssue, getTextPreview } from '../../../core/models/article-entry.model';
import { ArticleEntryDialogComponent } from '../article-entry-dialog/article-entry-dialog.component';
import { ArticleDetailsDialogComponent } from '../article-details-dialog/article-details-dialog.component';

@Component({
  selector: 'app-rhenanenruf-glossar',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatPaginatorModule,
    MatMenuModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatTooltipModule
  ],
  templateUrl: './rhenanenruf-glossar.component.html',
  styleUrl: './rhenanenruf-glossar.component.scss'
})
export class RhenanenrufGlossarComponent implements OnInit {
  private articleService = inject(ArticleEntryService);
  private router = inject(Router);
  private authService = inject(AuthService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);
  themeService = inject(ThemeService);

  currentUser = this.authService.getCurrentUser();

  articles = signal<ArticleEntry[]>([]);
  categories = signal<string[]>([]);
  years = signal<number[]>([]);

  loading = signal<boolean>(false);
  searchTerm = '';
  selectedCategory = '';
  selectedYear: number | null = null;

  // Pagination
  totalElements = 0;
  totalPages = 0;
  pageSize = 20;
  currentPage = 0;

  ngOnInit(): void {
    this.loadArticles();
    this.loadFilters();
  }

  loadArticles(): void {
    this.loading.set(true);

    if (this.searchTerm) {
      this.articleService.searchArticles(this.searchTerm, this.currentPage, this.pageSize)
        .subscribe({
          next: (response) => {
            this.articles.set(response.content);
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            this.loading.set(false);
          },
          error: (error) => {
            console.error('Error loading articles:', error);
            this.loading.set(false);
          }
        });
    } else if (this.selectedCategory) {
      this.articleService.getArticlesByCategory(this.selectedCategory, this.currentPage, this.pageSize)
        .subscribe({
          next: (response) => {
            this.articles.set(response.content);
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            this.loading.set(false);
          },
          error: (error) => {
            console.error('Error loading articles:', error);
            this.loading.set(false);
          }
        });
    } else {
      this.articleService.getAllArticles(this.currentPage, this.pageSize)
        .subscribe({
          next: (response) => {
            this.articles.set(response.content);
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            this.loading.set(false);
          },
          error: (error) => {
            console.error('Error loading articles:', error);
            this.loading.set(false);
          }
        });
    }
  }

  loadFilters(): void {
    this.articleService.getAllCategories().subscribe({
      next: (categories) => this.categories.set(categories),
      error: (error) => console.error('Error loading categories:', error)
    });

    this.articleService.getAllYears().subscribe({
      next: (years) => this.years.set(years),
      error: (error) => console.error('Error loading years:', error)
    });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.selectedCategory = '';
    this.selectedYear = null;
    this.loadArticles();
  }

  onCategoryChange(): void {
    this.currentPage = 0;
    this.searchTerm = '';
    this.selectedYear = null;
    this.loadArticles();
  }

  onYearChange(): void {
    this.currentPage = 0;
    if (this.selectedYear) {
      this.loading.set(true);
      this.articleService.getArticlesByYear(this.selectedYear).subscribe({
        next: (articles) => {
          this.articles.set(articles);
          this.totalElements = articles.length;
          this.totalPages = 1;
          this.loading.set(false);
        },
        error: (error) => {
          console.error('Error loading articles by year:', error);
          this.loading.set(false);
        }
      });
    } else {
      this.loadArticles();
    }
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadArticles();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.selectedCategory = '';
    this.selectedYear = null;
    this.currentPage = 0;
    this.loadArticles();
  }

  getFormattedIssue(article: ArticleEntry): string {
    return getFormattedIssue(article);
  }

  getTextPreview(article: ArticleEntry): string {
    return getTextPreview(article);
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  logout(): void {
    this.authService.logout();
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(ArticleEntryDialogComponent, {
      width: '600px',
      data: {
        mode: 'create',
        categories: this.categories()
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.articleService.createArticle(result).subscribe({
          next: () => {
            this.snackBar.open('Artikel erfolgreich erstellt', 'OK', { duration: 3000 });
            this.loadArticles();
          },
          error: (error) => {
            console.error('Error creating article:', error);
            this.snackBar.open('Fehler beim Erstellen des Artikels', 'OK', { duration: 3000 });
          }
        });
      }
    });
  }

  openEditDialog(article: ArticleEntry): void {
    const dialogRef = this.dialog.open(ArticleEntryDialogComponent, {
      width: '600px',
      data: {
        mode: 'edit',
        article: article,
        categories: this.categories()
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.id) {
        this.articleService.updateArticle(result.id, result).subscribe({
          next: () => {
            this.snackBar.open('Artikel erfolgreich aktualisiert', 'OK', { duration: 3000 });
            this.loadArticles();
          },
          error: (error) => {
            console.error('Error updating article:', error);
            this.snackBar.open('Fehler beim Aktualisieren des Artikels', 'OK', { duration: 3000 });
          }
        });
      }
    });
  }

  deleteArticle(article: ArticleEntry): void {
    if (confirm(`Möchten Sie den Artikel "${article.title}" wirklich löschen?`)) {
      this.articleService.deleteArticle(article.id).subscribe({
        next: () => {
          this.snackBar.open('Artikel erfolgreich gelöscht', 'OK', { duration: 3000 });
          this.loadArticles();
        },
        error: (error) => {
          console.error('Error deleting article:', error);
          this.snackBar.open('Fehler beim Löschen des Artikels', 'OK', { duration: 3000 });
        }
      });
    }
  }

  openDetailsDialog(article: ArticleEntry): void {
    this.dialog.open(ArticleDetailsDialogComponent, {
      width: '800px',
      maxWidth: '90vw',
      data: article
    });
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
