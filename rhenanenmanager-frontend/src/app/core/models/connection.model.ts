export enum ConnectionType {
  LEIBBURSCH = 'LEIBBURSCH',
  LEIBFUCHS = 'LEIBFUCHS',
  MENTOR = 'MENTOR',
  MENTEE = 'MENTEE',
  FREUNDSCHAFT = 'FREUNDSCHAFT',
  BRUDERSCHAFT = 'BRUDERSCHAFT'
}

export interface Profile {
  id: number;
  firstname: string;
  lastname: string;
  number?: number;
  status?: string;
  profileImageUrl?: string;
  receptionDate?: string;
}

export interface Connection {
  id: number;
  fromProfileId: number;
  toProfileId: number;
  connectionType: ConnectionType;
  description?: string;
  startDate?: string;
  endDate?: string;
  active: boolean;
}

export interface ConnectionWithProfiles {
  connection: Connection;
  fromProfile: Profile;
  toProfile: Profile;
}

export interface ConnectionGraphNode {
  id: number;
  profile: Profile;
  x: number;
  y: number;
  connections: Connection[];
}

export interface CreateConnectionRequest {
  fromProfileId: number;
  toProfileId: number;
  connectionType: ConnectionType;
  description?: string;
  startDate?: string;
}

export interface UpdateConnectionRequest {
  connectionType?: ConnectionType;
  description?: string;
  endDate?: string;
  active?: boolean;
}

export const CONNECTION_TYPE_LABELS: Record<ConnectionType, string> = {
  [ConnectionType.LEIBBURSCH]: 'Leibbursch',
  [ConnectionType.LEIBFUCHS]: 'Leibfuchs',
  [ConnectionType.MENTOR]: 'Mentor',
  [ConnectionType.MENTEE]: 'Mentee',
  [ConnectionType.FREUNDSCHAFT]: 'Freundschaft',
  [ConnectionType.BRUDERSCHAFT]: 'Bruderschaft'
};

export const CONNECTION_TYPE_COLORS: Record<ConnectionType, string> = {
  [ConnectionType.LEIBBURSCH]: '#06b6d4',
  [ConnectionType.LEIBFUCHS]: '#7c3aed',
  [ConnectionType.MENTOR]: '#f59e0b',
  [ConnectionType.MENTEE]: '#10b981',
  [ConnectionType.FREUNDSCHAFT]: '#ec4899',
  [ConnectionType.BRUDERSCHAFT]: '#1e3a8a'
};

export const CONNECTION_TYPE_ICONS: Record<ConnectionType, string> = {
  [ConnectionType.LEIBBURSCH]: 'supervisor_account',
  [ConnectionType.LEIBFUCHS]: 'person_add',
  [ConnectionType.MENTOR]: 'school',
  [ConnectionType.MENTEE]: 'account_circle',
  [ConnectionType.FREUNDSCHAFT]: 'favorite',
  [ConnectionType.BRUDERSCHAFT]: 'groups'
};
