# Connections Feature - Beziehungsvisualisierung

## Übersicht

Das Connections-Feature bietet eine interaktive Visualisierung der Beziehungen zwischen Corpsbrüdern. Es ermöglicht die Verwaltung und Darstellung verschiedener Beziehungstypen wie Leibbursch-Leibfuchs, Mentor-Mentee, und Freundschaften.

## Features

### 1. Interaktive Graph-Visualisierung
- **SVG-basierte Darstellung** mit automatischem Force-Directed-Layout
- **Drag & Drop** - Nodes können verschoben werden
- **Zoom & Pan** - Volle Interaktivität
- **Highlighting** - Klick auf einen Node hebt verbundene Nodes hervor
- **Tooltips** - Details zu Beziehungen beim Hover
- **Responsive Design** - Funktioniert auf allen Bildschirmgrößen

### 2. Beziehungstypen

Das System unterstützt folgende Beziehungstypen:

| Typ | Beschreibung | Farbe | Icon |
|-----|-------------|-------|------|
| LEIBBURSCH | Älteres Corps-Mitglied, das einen Fuchs betreut | Cyan (#06b6d4) | supervisor_account |
| LEIBFUCHS | Fuchs, der von einem Leibburschen betreut wird | Lila (#7c3aed) | person_add |
| MENTOR | Mentor-Beziehung | Orange (#f59e0b) | school |
| MENTEE | Mentee in einer Mentor-Beziehung | Grün (#10b981) | account_circle |
| FREUNDSCHAFT | Freundschaftliche Beziehung | Pink (#ec4899) | favorite |
| BRUDERSCHAFT | Bruderschaftliche Verbindung | Dunkelblau (#1e3a8a) | groups |

### 3. CRUD-Operationen

#### Erstellen
- Dialog-basierte Erstellung neuer Beziehungen
- Autocomplete-Suche für Mitglieder
- Optionale Beschreibung und Datumsangaben
- Validierung der Eingaben

#### Bearbeiten
- Bearbeitung von Typ, Beschreibung und Datum
- Source/Target können nicht geändert werden

#### Löschen
- Bestätigungsdialog vor dem Löschen
- Erfolgs-/Fehlermeldungen

### 4. Filter & Ansichten

#### Filter
- **Typ-Filter** - Zeige nur bestimmte Beziehungstypen
- **Multi-Select** - Mehrere Typen gleichzeitig filtern
- **Visual Feedback** - Farbcodierte Chips

#### Ansichten
- **Graph-Ansicht** - Interaktive Visualisierung
- **Listen-Ansicht** - Tabellarische Übersicht mit Cards
- **Toggle** - Einfacher Wechsel zwischen Ansichten

### 5. Dark Mode Support

Vollständige Unterstützung für Light/Dark Theme:
- Automatische Farbanpassung
- Kontrastreiche Darstellung
- Lesbare Texte in beiden Modi

## Technische Details

### Komponenten-Struktur

```
features/connections/
├── connections-overview/          # Hauptkomponente
│   ├── connections-overview.component.ts
│   ├── connections-overview.component.html
│   └── connections-overview.component.scss
├── connections-graph/            # Graph-Visualisierung
│   ├── connections-graph.component.ts
│   ├── connections-graph.component.html
│   └── connections-graph.component.scss
├── connection-dialog/            # Create/Edit Dialog
│   ├── connection-dialog.component.ts
│   ├── connection-dialog.component.html
│   └── connection-dialog.component.scss
├── connections.routes.ts         # Route Definitionen
└── README.md                     # Diese Datei
```

### Services

**ConnectionService** (`core/services/connection.service.ts`)
- getAllConnections() - Alle Beziehungen laden
- getConnectionsByProfile() - Beziehungen für ein Mitglied
- getConnectionsByType() - Beziehungen nach Typ filtern
- createConnection() - Neue Beziehung erstellen
- updateConnection() - Beziehung aktualisieren
- deleteConnection() - Beziehung löschen
- getAllProfiles() - Alle Profile laden
- searchProfiles() - Profile suchen

### Models

**Connection Models** (`core/models/connection.model.ts`)
- `ConnectionType` - Enum für Beziehungstypen
- `Profile` - Interface für Mitglieder
- `Connection` - Interface für Beziehungen
- `ConnectionWithProfiles` - Connection mit vollständigen Profilen
- `ConnectionGraphNode` - Node für Graph-Darstellung
- `CreateConnectionRequest` - Request DTO
- `UpdateConnectionRequest` - Update DTO

### Graph-Algorithmus

Die Visualisierung verwendet ein **Force-Directed Graph Layout**:

1. **Kräfte**:
   - **Center Force** - Zieht Nodes zum Mittelpunkt
   - **Charge Force** - Repulsion zwischen Nodes
   - **Link Force** - Attraction zwischen verbundenen Nodes

2. **Animation**:
   - RequestAnimationFrame für smooth 60fps
   - Velocity-basierte Bewegung mit Dämpfung
   - Boundary-Checking für Viewport

3. **Interaktion**:
   - Drag & Drop mit Maus
   - Click-to-Select
   - Hover-Effects

## API-Endpoints

Das Feature kommuniziert mit folgenden Backend-Endpoints:

```
GET    /api/connections                   # Alle Beziehungen
GET    /api/connections/profile/{id}      # Beziehungen eines Profils
GET    /api/connections/type/{type}       # Beziehungen nach Typ
GET    /api/connections/{id}              # Einzelne Beziehung
POST   /api/connections                   # Beziehung erstellen
PUT    /api/connections/{id}              # Beziehung aktualisieren
DELETE /api/connections/{id}              # Beziehung löschen
GET    /api/profiles                      # Alle Profile
GET    /api/profiles/search?query=...     # Profile suchen
```

## Zukünftige Erweiterungen

Geplante Features:

1. **Export-Funktionen**
   - PDF-Export des Graphen
   - CSV-Export der Beziehungen
   - Bild-Export (PNG/SVG)

2. **Erweiterte Filter**
   - Zeitraum-Filter (aktiv/historisch)
   - Status-Filter (Fuchs, Bursche, AH)
   - Kombinierte Filter

3. **Statistiken**
   - Dashboard mit Metriken
   - Häufigste Beziehungstypen
   - Netzwerk-Dichte
   - Zentralitäts-Metriken

4. **Layout-Optionen**
   - Hierarchisches Layout
   - Radiales Layout
   - Custom Positionierung speichern

5. **Batch-Operationen**
   - Mehrere Beziehungen auf einmal erstellen
   - Import aus CSV/Excel
   - Template-basierte Erstellung

## Verwendung

### Navigation

Vom Dashboard aus:
```typescript
this.router.navigate(['/connections']);
```

Oder über die Dashboard-Karte "Beziehungen" klicken.

### Neue Beziehung erstellen

1. Klick auf "Neue Beziehung" Button
2. Wähle Source-Mitglied aus Autocomplete
3. Wähle Beziehungstyp aus Dropdown
4. Wähle Target-Mitglied aus Autocomplete
5. Optional: Beschreibung und Datum hinzufügen
6. Klick auf "Erstellen"

### Beziehung bearbeiten

**Graph-Ansicht:**
1. Klick auf einen Node
2. In der aufklappenden Card auf "Bearbeiten" klicken
3. Änderungen vornehmen
4. Speichern

**Listen-Ansicht:**
1. Klick auf "Bearbeiten" Button der Card
2. Änderungen vornehmen
3. Speichern

### Filtern

1. Klick auf gewünschte Beziehungstyp-Chips
2. Mehrfachauswahl möglich
3. Graph/Liste aktualisiert sich automatisch

## Debugging

**Console Logs aktivieren:**
```typescript
// Im Component
console.log('Connections:', this.connections());
console.log('Nodes:', this.nodes());
console.log('Links:', this.links());
```

**Netzwerk-Requests prüfen:**
- Browser DevTools → Network Tab
- Filter: "connections" oder "profiles"
- Check Response und Status Codes

## Performance

**Optimierungen:**
- Signals für reactive state management
- OnPush change detection strategy
- Lazy loading des Feature-Moduls
- Throttled force simulation
- Virtualized scrolling in Listen-Ansicht (geplant)

**Empfohlene Limits:**
- < 100 Nodes: Optimale Performance
- 100-500 Nodes: Gute Performance
- > 500 Nodes: Consider pagination/clustering

## Styling

**CSS-Variablen für Anpassungen:**
```scss
:root {
  --connection-leibbursch: #06b6d4;
  --connection-leibfuchs: #7c3aed;
  --connection-mentor: #f59e0b;
  --connection-mentee: #10b981;
  --connection-freundschaft: #ec4899;
  --connection-bruderschaft: #1e3a8a;
}
```

## Accessibility

- Keyboard Navigation (geplant)
- ARIA Labels auf interaktiven Elementen
- Screen Reader Support
- High Contrast Mode Support
- Focus Indicators

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Lizenz

Teil des RhenanenManager-Projekts.
