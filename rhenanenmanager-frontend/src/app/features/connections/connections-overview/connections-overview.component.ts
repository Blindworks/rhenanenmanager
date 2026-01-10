import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { Router } from '@angular/router';
import { ConnectionService } from '../../../core/services/connection.service';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService } from '../../../core/services/theme.service';
import {
  ConnectionWithProfiles,
  ConnectionType,
  CONNECTION_TYPE_LABELS,
  CONNECTION_TYPE_COLORS
} from '../../../core/models/connection.model';
import { ConnectionsGraphComponent } from '../connections-graph/connections-graph.component';
import { ConnectionDialogComponent } from '../connection-dialog/connection-dialog.component';

@Component({
  selector: 'app-connections-overview',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule,
    MatMenuModule,
    MatTooltipModule,
    MatDividerModule,
    ConnectionsGraphComponent
  ],
  templateUrl: './connections-overview.component.html',
  styleUrl: './connections-overview.component.scss'
})
export class ConnectionsOverviewComponent implements OnInit {
  private connectionService = inject(ConnectionService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  themeService = inject(ThemeService);
  currentUser = this.authService.getCurrentUser();

  connections = signal<ConnectionWithProfiles[]>([]);
  loading = signal<boolean>(true);
  selectedTypes = signal<ConnectionType[]>([]);
  viewMode = signal<'graph' | 'list'>('graph');

  connectionTypes = Object.values(ConnectionType);
  connectionTypeLabels = CONNECTION_TYPE_LABELS;
  connectionTypeColors = CONNECTION_TYPE_COLORS;

  ngOnInit(): void {
    this.loadConnections();
  }

  loadConnections(): void {
    this.loading.set(true);
    this.connectionService.getAllConnections().subscribe({
      next: (data) => {
        this.connections.set(data);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Failed to load connections:', error);
        this.snackBar.open('Fehler beim Laden der Beziehungen', 'Schließen', {
          duration: 3000
        });
        this.loading.set(false);
      }
    });
  }

  filteredConnections(): ConnectionWithProfiles[] {
    const selected = this.selectedTypes();
    if (selected.length === 0) {
      return this.connections();
    }
    return this.connections().filter(c => selected.includes(c.connection.connectionType));
  }

  toggleTypeFilter(type: ConnectionType): void {
    const current = this.selectedTypes();
    const index = current.indexOf(type);

    if (index >= 0) {
      this.selectedTypes.set(current.filter(t => t !== type));
    } else {
      this.selectedTypes.set([...current, type]);
    }
  }

  isTypeSelected(type: ConnectionType): boolean {
    return this.selectedTypes().includes(type);
  }

  toggleViewMode(): void {
    this.viewMode.set(this.viewMode() === 'graph' ? 'list' : 'graph');
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(ConnectionDialogComponent, {
      width: '600px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadConnections();
      }
    });
  }

  openEditDialog(connection: ConnectionWithProfiles): void {
    const dialogRef = this.dialog.open(ConnectionDialogComponent, {
      width: '600px',
      data: {
        mode: 'edit',
        connection: connection
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadConnections();
      }
    });
  }

  deleteConnection(connection: ConnectionWithProfiles): void {
    if (confirm(`Beziehung zwischen ${connection.fromProfile.firstname} ${connection.fromProfile.lastname} und ${connection.toProfile.firstname} ${connection.toProfile.lastname} wirklich löschen?`)) {
      this.connectionService.deleteConnection(connection.connection.id).subscribe({
        next: () => {
          this.snackBar.open('Beziehung erfolgreich gelöscht', 'Schließen', {
            duration: 3000
          });
          this.loadConnections();
        },
        error: (error) => {
          console.error('Failed to delete connection:', error);
          this.snackBar.open('Fehler beim Löschen der Beziehung', 'Schließen', {
            duration: 3000
          });
        }
      });
    }
  }

  getTypeColor(type: ConnectionType): string {
    return this.connectionTypeColors[type];
  }

  getTypeLabel(type: ConnectionType): string {
    return this.connectionTypeLabels[type];
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  logout(): void {
    this.authService.logout();
  }
}
