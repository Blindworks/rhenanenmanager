import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatToolbarModule
  ],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.scss'
})
export class WelcomeComponent {
  features = [
    {
      icon: 'groups',
      title: 'Mitgliederverwaltung',
      description: 'Verwalten Sie Mitgliederdaten, Corps-Laufbahn und Status zentral an einem Ort.'
    },
    {
      icon: 'sports_martial_arts',
      title: 'Mensur-Tracking',
      description: 'Detailliertes Protokollieren und Auswerten von Mensuren mit über 50 technischen Metriken.'
    },
    {
      icon: 'event',
      title: 'Veranstaltungen',
      description: 'Planen Sie Events, verwalten Sie Zusagen und behalten Sie die Anwesenheit im Blick.'
    },
    {
      icon: 'forum',
      title: 'Kommunikation',
      description: 'Internes Forum, E-Mail-Verteiler und News-System für effektive Kommunikation.'
    },
    {
      icon: 'work',
      title: 'Karriere-Netzwerk',
      description: 'Nutzen Sie das Alumni-Netzwerk mit Stellenangeboten und beruflichen Kontakten.'
    },
    {
      icon: 'folder',
      title: 'Dokumenten-Archiv',
      description: 'Zentrale Ablage für Satzungen, Protokolle, Artikel und historische Dokumente.'
    }
  ];

  constructor(private router: Router) {}

  navigateToLogin(): void {
    this.router.navigate(['/auth/login']);
  }

  navigateToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  scrollToFeatures(): void {
    const element = document.getElementById('features');
    element?.scrollIntoView({ behavior: 'smooth' });
  }
}
