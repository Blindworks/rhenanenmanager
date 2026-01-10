import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { LoginRequest, AuthResponse } from '../models/auth.model';
import { ROLES } from '../models/role.constants';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'auth_user';

  private authStateSubject = new BehaviorSubject<boolean>(this.isAuthenticated());
  public authState$ = this.authStateSubject.asObservable();

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.USER_KEY, JSON.stringify({
          username: response.username,
          email: response.email,
          role: response.role
        }));
        this.authStateSubject.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.authStateSubject.next(false);
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getCurrentUser() {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  /**
   * Check if the current user has a specific role
   * @param role The role name to check (e.g., 'ROLE_ADMIN')
   * @returns true if user has the role, false otherwise
   */
  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user?.role === role;
  }

  /**
   * Check if the current user is an admin
   * @returns true if user has ROLE_ADMIN, false otherwise
   */
  isAdmin(): boolean {
    return this.hasRole(ROLES.ADMIN);
  }

  /**
   * Check if the current user is a regular user
   * @returns true if user has ROLE_USER, false otherwise
   */
  isUser(): boolean {
    return this.hasRole(ROLES.USER);
  }

  /**
   * Check if the current user is a moderator
   * @returns true if user has ROLE_MODERATOR, false otherwise
   */
  isModerator(): boolean {
    return this.hasRole(ROLES.MODERATOR);
  }
}
