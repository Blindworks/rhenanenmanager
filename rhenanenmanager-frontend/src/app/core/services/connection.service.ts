import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  Connection,
  ConnectionWithProfiles,
  CreateConnectionRequest,
  UpdateConnectionRequest,
  ConnectionType,
  Profile
} from '../models/connection.model';

interface BackendConnectionResponse {
  id: number;
  fromProfileId: number;
  fromProfileName: string;
  toProfileId: number;
  toProfileName: string;
  relationType: string;
  startDate?: string;
  endDate?: string;
  description?: string;
  bidirectional?: boolean;
  active: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {
  private http = inject(HttpClient);
  private apiUrl = '/api/connections';

  getAllConnections(activeOnly: boolean = true): Observable<ConnectionWithProfiles[]> {
    const params = new HttpParams().set('activeOnly', activeOnly.toString());
    return this.http.get<BackendConnectionResponse[]>(this.apiUrl, { params }).pipe(
      map(responses => responses.map(r => this.mapToConnectionWithProfiles(r)))
    );
  }

  private mapToConnectionWithProfiles(response: BackendConnectionResponse): ConnectionWithProfiles {
    const names = response.fromProfileName.split(' ');
    const fromFirstname = names[0] || '';
    const fromLastname = names.slice(1).join(' ') || '';

    const toNames = response.toProfileName.split(' ');
    const toFirstname = toNames[0] || '';
    const toLastname = toNames.slice(1).join(' ') || '';

    return {
      connection: {
        id: response.id,
        fromProfileId: response.fromProfileId,
        toProfileId: response.toProfileId,
        connectionType: response.relationType as ConnectionType,
        description: response.description,
        startDate: response.startDate,
        endDate: response.endDate,
        active: response.active
      },
      fromProfile: {
        id: response.fromProfileId,
        firstname: fromFirstname,
        lastname: fromLastname
      },
      toProfile: {
        id: response.toProfileId,
        firstname: toFirstname,
        lastname: toLastname
      }
    };
  }

  getConnectionsByProfile(profileId: number, activeOnly: boolean = true): Observable<ConnectionWithProfiles[]> {
    const params = new HttpParams().set('activeOnly', activeOnly.toString());
    return this.http.get<BackendConnectionResponse[]>(`${this.apiUrl}/profile/${profileId}`, { params }).pipe(
      map(responses => responses.map(r => this.mapToConnectionWithProfiles(r)))
    );
  }

  getConnectionsByType(connectionType: ConnectionType, activeOnly: boolean = true): Observable<ConnectionWithProfiles[]> {
    const params = new HttpParams().set('activeOnly', activeOnly.toString());
    return this.http.get<BackendConnectionResponse[]>(`${this.apiUrl}/type/${connectionType}`, { params }).pipe(
      map(responses => responses.map(r => this.mapToConnectionWithProfiles(r)))
    );
  }

  getConnectionById(id: number): Observable<ConnectionWithProfiles> {
    return this.http.get<BackendConnectionResponse>(`${this.apiUrl}/${id}`).pipe(
      map(r => this.mapToConnectionWithProfiles(r))
    );
  }

  createConnection(request: CreateConnectionRequest): Observable<Connection> {
    return this.http.post<Connection>(this.apiUrl, request);
  }

  updateConnection(id: number, request: UpdateConnectionRequest): Observable<Connection> {
    return this.http.put<Connection>(`${this.apiUrl}/${id}`, request);
  }

  deleteConnection(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getAllProfiles(): Observable<Profile[]> {
    return this.http.get<Profile[]>('/api/profiles');
  }

  searchProfiles(query: string): Observable<Profile[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<Profile[]>('/api/profiles/search', { params });
  }
}
