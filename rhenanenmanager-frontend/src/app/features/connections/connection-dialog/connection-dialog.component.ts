import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { debounceTime, distinctUntilChanged, switchMap, of } from 'rxjs';
import { ConnectionService } from '../../../core/services/connection.service';
import {
  ConnectionType,
  ConnectionWithProfiles,
  Profile,
  CONNECTION_TYPE_LABELS,
  CONNECTION_TYPE_ICONS
} from '../../../core/models/connection.model';

export interface ConnectionDialogData {
  mode: 'create' | 'edit';
  connection?: ConnectionWithProfiles;
}

@Component({
  selector: 'app-connection-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatAutocompleteModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './connection-dialog.component.html',
  styleUrl: './connection-dialog.component.scss'
})
export class ConnectionDialogComponent implements OnInit {
  private dialogRef = inject(MatDialogRef<ConnectionDialogComponent>);
  private connectionService = inject(ConnectionService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);
  data: ConnectionDialogData = inject(MAT_DIALOG_DATA);

  form!: FormGroup;
  connectionTypes = Object.values(ConnectionType);
  connectionTypeLabels = CONNECTION_TYPE_LABELS;
  connectionTypeIcons = CONNECTION_TYPE_ICONS;

  fromProfiles = signal<Profile[]>([]);
  toProfiles = signal<Profile[]>([]);
  loading = signal<boolean>(false);
  searchingFrom = signal<boolean>(false);
  searchingTo = signal<boolean>(false);

  get isEditMode(): boolean {
    return this.data.mode === 'edit';
  }

  get dialogTitle(): string {
    return this.isEditMode ? 'Beziehung bearbeiten' : 'Neue Beziehung erstellen';
  }

  ngOnInit(): void {
    this.initForm();
    this.setupAutocomplete();
    this.loadAllProfiles();
  }

  private initForm(): void {
    if (this.isEditMode && this.data.connection) {
      const conn = this.data.connection.connection;
      this.form = this.fb.group({
        fromProfile: [this.data.connection.fromProfile, Validators.required],
        toProfile: [this.data.connection.toProfile, Validators.required],
        connectionType: [conn.connectionType, Validators.required],
        description: [conn.description || ''],
        startDate: [conn.startDate ? new Date(conn.startDate) : null],
        endDate: [conn.endDate ? new Date(conn.endDate) : null]
      });
    } else {
      this.form = this.fb.group({
        fromProfile: [null, Validators.required],
        toProfile: [null, Validators.required],
        connectionType: ['', Validators.required],
        description: [''],
        startDate: [null],
        endDate: [null]
      });
    }
  }

  private setupAutocomplete(): void {
    this.form.get('fromProfile')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(value => {
          if (typeof value === 'string' && value.length > 1) {
            this.searchingFrom.set(true);
            return this.connectionService.searchProfiles(value);
          }
          return of([]);
        })
      )
      .subscribe({
        next: profiles => {
          this.fromProfiles.set(profiles);
          this.searchingFrom.set(false);
        },
        error: () => this.searchingFrom.set(false)
      });

    this.form.get('toProfile')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(value => {
          if (typeof value === 'string' && value.length > 1) {
            this.searchingTo.set(true);
            return this.connectionService.searchProfiles(value);
          }
          return of([]);
        })
      )
      .subscribe({
        next: profiles => {
          this.toProfiles.set(profiles);
          this.searchingTo.set(false);
        },
        error: () => this.searchingTo.set(false)
      });
  }

  private loadAllProfiles(): void {
    this.connectionService.getAllProfiles().subscribe({
      next: profiles => {
        this.fromProfiles.set(profiles);
        this.toProfiles.set(profiles);
      },
      error: error => {
        console.error('Failed to load profiles:', error);
        this.snackBar.open('Fehler beim Laden der Profile', 'Schließen', {
          duration: 3000
        });
      }
    });
  }

  displayProfile(profile: Profile | null): string {
    if (!profile) return '';
    return `${profile.firstname} ${profile.lastname}${profile.number ? ' (' + profile.number + ')' : ''}`;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      Object.keys(this.form.controls).forEach(key => {
        this.form.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading.set(true);
    const formValue = this.form.value;

    if (this.isEditMode && this.data.connection) {
      const updateRequest = {
        connectionType: formValue.connectionType,
        description: formValue.description || undefined,
        endDate: formValue.endDate ? formValue.endDate.toISOString().split('T')[0] : undefined,
        active: !formValue.endDate
      };

      this.connectionService.updateConnection(this.data.connection.connection.id, updateRequest).subscribe({
        next: () => {
          this.snackBar.open('Beziehung erfolgreich aktualisiert', 'Schließen', {
            duration: 3000
          });
          this.dialogRef.close(true);
        },
        error: error => {
          console.error('Failed to update connection:', error);
          this.snackBar.open('Fehler beim Aktualisieren der Beziehung', 'Schließen', {
            duration: 3000
          });
          this.loading.set(false);
        }
      });
    } else {
      const createRequest = {
        fromProfileId: formValue.fromProfile.id,
        toProfileId: formValue.toProfile.id,
        connectionType: formValue.connectionType,
        description: formValue.description || undefined,
        startDate: formValue.startDate ? formValue.startDate.toISOString().split('T')[0] : undefined
      };

      this.connectionService.createConnection(createRequest).subscribe({
        next: () => {
          this.snackBar.open('Beziehung erfolgreich erstellt', 'Schließen', {
            duration: 3000
          });
          this.dialogRef.close(true);
        },
        error: error => {
          console.error('Failed to create connection:', error);
          this.snackBar.open('Fehler beim Erstellen der Beziehung', 'Schließen', {
            duration: 3000
          });
          this.loading.set(false);
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
