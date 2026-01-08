import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ThemeService } from '../../core/services/theme.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatMenuModule,
    MatDividerModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  themeService = inject(ThemeService);

  currentUser = this.authService.getCurrentUser();

  stats = [
    { icon: 'groups', label: 'Mitglieder', value: '156', color: '#06b6d4' },
    { icon: 'event', label: 'Events', value: '42', color: '#7c3aed' },
    { icon: 'sports_martial_arts', label: 'Mensuren', value: '28', color: '#f59e0b' },
    { icon: 'folder', label: 'Dokumente', value: '234', color: '#1e3a8a' }
  ];

  features = [
    {
      icon: 'groups',
      title: 'Mitgliederverwaltung',
      description: 'Verwalten Sie Profile, Status und Corps-Laufbahn',
      route: '/members'
    },
    {
      icon: 'sports_martial_arts',
      title: 'Mensur-Tracking',
      description: 'Protokollieren und auswerten Sie Mensuren',
      route: '/mensur'
    },
    {
      icon: 'event',
      title: 'Veranstaltungen',
      description: 'Planen Sie Events und verwalten Sie Zusagen',
      route: '/events'
    },
    {
      icon: 'library_books',
      title: 'Rhenanenruf Glossar',
      description: 'Durchsuchen Sie das Archiv des Rhenanenrufs',
      route: '/rhenanenruf-glossar'
    },
    {
      icon: 'forum',
      title: 'Forum',
      description: 'Diskutieren Sie mit der Community',
      route: '/forum'
    },
    {
      icon: 'work',
      title: 'Karriere-Netzwerk',
      description: 'Nutzen Sie das Alumni-Netzwerk',
      route: '/career'
    },
    {
      icon: 'folder',
      title: 'Dokumente',
      description: 'Zugriff auf Satzungen und Protokolle',
      route: '/documents'
    }
  ];

  recentActivities = [
    { icon: 'person_add', text: 'Neues Mitglied aufgenommen: Johann Weber', time: 'vor 2 Stunden' },
    { icon: 'event', text: 'Neue Veranstaltung: Sommerkneipe 2026', time: 'vor 5 Stunden' },
    { icon: 'sports_martial_arts', text: 'Mensur-Protokoll aktualisiert', time: 'vor 1 Tag' }
  ];

  goToProfile(): void {
    this.router.navigate(['/profile']);
  }

  navigateToFeature(route: string): void {
    this.router.navigate([route]);
  }

  logout(): void {
    this.authService.logout();
  }
}
