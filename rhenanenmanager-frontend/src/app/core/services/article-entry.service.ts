import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ArticleEntry, ArticleEntryPage } from '../models/article-entry.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ArticleEntryService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/articles`;

  /**
   * Get all articles with pagination.
   */
  getAllArticles(page: number = 0, size: number = 20): Observable<ArticleEntryPage> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ArticleEntryPage>(this.apiUrl, { params });
  }

  /**
   * Get article by ID.
   */
  getArticleById(id: number): Observable<ArticleEntry> {
    return this.http.get<ArticleEntry>(`${this.apiUrl}/${id}`);
  }

  /**
   * Get articles by year.
   */
  getArticlesByYear(year: number): Observable<ArticleEntry[]> {
    return this.http.get<ArticleEntry[]>(`${this.apiUrl}/year/${year}`);
  }

  /**
   * Get articles by category.
   */
  getArticlesByCategory(category: string, page: number = 0, size: number = 20): Observable<ArticleEntryPage> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ArticleEntryPage>(`${this.apiUrl}/category/${category}`, { params });
  }

  /**
   * Search articles by keyword.
   */
  searchArticles(keyword: string, page: number = 0, size: number = 20): Observable<ArticleEntryPage> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ArticleEntryPage>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Get all distinct categories.
   */
  getAllCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/categories`);
  }

  /**
   * Get all distinct years.
   */
  getAllYears(): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/years`);
  }
}
