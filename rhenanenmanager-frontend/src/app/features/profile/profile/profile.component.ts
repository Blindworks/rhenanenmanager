import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService } from '../../../core/services/theme.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    MatToolbarModule,
    MatMenuModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  themeService = inject(ThemeService);

  currentUser = this.authService.getCurrentUser();

  // Personal Information
  personalInfo = {
    title: 'Dipl.-Ing.',
    firstname: this.currentUser?.firstname || 'Max',
    lastname: this.currentUser?.lastname || 'Mustermann',
    birthDate: '15.03.1998',
    birthPlace: 'Heidelberg',
    email: this.currentUser?.email || 'max.mustermann@example.de',
    phone: '+49 151 23456789',
    mobile: '+49 151 98765432'
  };

  // Corps Membership Data
  corpsMembership = {
    status: 'Alter Herr',
    number: '1234',
    reception: '15.04.2018',
    acception: '20.10.2019',
    philistrierung: '30.09.2023',
    leibbursch: 'Hans Schmidt',
    leibfuchs: 'Johann Weber'
  };

  // Corps Positions/Offices
  positions = [
    { name: 'Senior', period: 'SS 2022 - WS 2022/23', active: false },
    { name: 'Fuchsmajor', period: 'WS 2021/22 - SS 2022', active: false }
  ];

  // Mensur Statistics
  mensurStats = {
    total: 8,
    successful: 7,
    pending: 0,
    lastDate: '12.05.2023'
  };

  // Career Information
  careerInfo = {
    education: 'Maschinenbau, KIT Karlsruhe',
    degree: 'Master of Science',
    employer: 'Daimler AG',
    position: 'Senior Engineer',
    department: 'Forschung & Entwicklung'
  };

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  logout(): void {
    this.authService.logout();
  }

  editProfile(): void {
    // TODO: Navigate to edit mode or open dialog
    console.log('Edit profile');
  }
}
