import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule
  ],
  template: `
    <mat-toolbar color="primary">
      <span>RhenanenManager</span>
      <span class="spacer"></span>
      <button mat-button (click)="logout()">
        <mat-icon>logout</mat-icon>
        Logout
      </button>
    </mat-toolbar>

    <div class="dashboard-container">
      <h1>Welcome, {{ currentUser?.username }}!</h1>
      <p>Role: {{ currentUser?.role }}</p>

      <div class="cards-container">
        <mat-card>
          <mat-card-header>
            <mat-card-title>Members</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <p>Manage member profiles and information</p>
          </mat-card-content>
        </mat-card>

        <mat-card>
          <mat-card-header>
            <mat-card-title>Events</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <p>View and manage upcoming events</p>
          </mat-card-content>
        </mat-card>

        <mat-card>
          <mat-card-header>
            <mat-card-title>Documents</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <p>Access important documents and files</p>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .spacer {
      flex: 1 1 auto;
    }

    .dashboard-container {
      padding: 20px;
    }

    .cards-container {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 20px;
      margin-top: 20px;
    }
  `]
})
export class DashboardComponent {
  private authService = inject(AuthService);
  currentUser = this.authService.getCurrentUser();

  logout(): void {
    this.authService.logout();
  }
}
