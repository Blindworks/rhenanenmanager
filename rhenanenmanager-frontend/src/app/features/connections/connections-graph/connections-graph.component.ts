import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  OnChanges,
  SimpleChanges,
  ElementRef,
  ViewChild,
  signal,
  computed
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import {
  ConnectionWithProfiles,
  Profile,
  ConnectionType,
  CONNECTION_TYPE_COLORS,
  CONNECTION_TYPE_LABELS,
  CONNECTION_TYPE_ICONS
} from '../../../core/models/connection.model';

interface GraphNode {
  id: number;
  profile: Profile;
  x: number;
  y: number;
  level?: number;
  column?: number;
}

interface GraphLink {
  source: GraphNode;
  target: GraphNode;
  connection: ConnectionWithProfiles;
}

@Component({
  selector: 'app-connections-graph',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatMenuModule
  ],
  templateUrl: './connections-graph.component.html',
  styleUrl: './connections-graph.component.scss'
})
export class ConnectionsGraphComponent implements OnInit, OnChanges {
  @Input() connections: ConnectionWithProfiles[] = [];
  @Output() editConnection = new EventEmitter<ConnectionWithProfiles>();
  @Output() deleteConnection = new EventEmitter<ConnectionWithProfiles>();

  @ViewChild('graphContainer', { static: false }) graphContainer!: ElementRef;

  nodes = signal<GraphNode[]>([]);
  links = signal<GraphLink[]>([]);
  selectedNode = signal<GraphNode | null>(null);
  hoveredNode = signal<GraphNode | null>(null);

  connectionTypeColors = CONNECTION_TYPE_COLORS;
  connectionTypeLabels = CONNECTION_TYPE_LABELS;
  connectionTypeIcons = CONNECTION_TYPE_ICONS;
  connectionTypes = Object.values(ConnectionType) as ConnectionType[];

  private width = 1200;
  private height = 800;

  ngOnInit(): void {
    this.buildGraph();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['connections'] && !changes['connections'].firstChange) {
      this.buildGraph();
    }
  }

  ngOnDestroy(): void {
    // No cleanup needed
  }

  private buildGraph(): void {
    const profileMap = new Map<number, GraphNode>();
    const nodesList: GraphNode[] = [];
    const linksList: GraphLink[] = [];

    this.connections.forEach(conn => {
      if (!profileMap.has(conn.fromProfile.id)) {
        const node = this.createNode(conn.fromProfile);
        profileMap.set(conn.fromProfile.id, node);
        nodesList.push(node);
      }

      if (!profileMap.has(conn.toProfile.id)) {
        const node = this.createNode(conn.toProfile);
        profileMap.set(conn.toProfile.id, node);
        nodesList.push(node);
      }

      const source = profileMap.get(conn.fromProfile.id)!;
      const target = profileMap.get(conn.toProfile.id)!;
      linksList.push({ source, target, connection: conn });
    });

    this.nodes.set(nodesList);
    this.links.set(linksList);
    this.layoutNodes();
  }

  private createNode(profile: Profile): GraphNode {
    return {
      id: profile.id,
      profile,
      x: 0,
      y: 0,
      level: 0,
      column: 0
    };
  }

  private layoutNodes(): void {
    const nodes = this.nodes();
    const links = this.links();
    if (nodes.length === 0) return;

    // Build adjacency lists for hierarchy detection
    const outgoing = new Map<number, Set<number>>();
    const incoming = new Map<number, Set<number>>();

    links.forEach(link => {
      const sourceId = link.source.id;
      const targetId = link.target.id;
      const type = link.connection.connection.connectionType;

      // For Leibbursch connections, create hierarchy
      if (type === ConnectionType.LEIBBURSCH) {
        if (!outgoing.has(sourceId)) outgoing.set(sourceId, new Set());
        if (!incoming.has(targetId)) incoming.set(targetId, new Set());
        outgoing.get(sourceId)!.add(targetId);
        incoming.get(targetId)!.add(sourceId);
      }
    });

    // Find root nodes (nodes with no incoming Leibbursch connections)
    const roots: GraphNode[] = [];
    const nodeMap = new Map(nodes.map(n => [n.id, n]));

    nodes.forEach(node => {
      if (!incoming.has(node.id) || incoming.get(node.id)!.size === 0) {
        roots.push(node);
      }
    });

    // Assign levels using BFS
    const visited = new Set<number>();
    const queue: Array<{ node: GraphNode; level: number }> = [];

    roots.forEach(root => {
      root.level = 0;
      queue.push({ node: root, level: 0 });
      visited.add(root.id);
    });

    let maxLevel = 0;
    while (queue.length > 0) {
      const { node, level } = queue.shift()!;
      maxLevel = Math.max(maxLevel, level);

      if (outgoing.has(node.id)) {
        outgoing.get(node.id)!.forEach(childId => {
          if (!visited.has(childId)) {
            const childNode = nodeMap.get(childId);
            if (childNode) {
              childNode.level = level + 1;
              queue.push({ node: childNode, level: level + 1 });
              visited.add(childId);
            }
          }
        });
      }
    }

    // Assign levels to unvisited nodes (not part of Leibbursch hierarchy)
    nodes.forEach(node => {
      if (!visited.has(node.id)) {
        node.level = maxLevel + 1;
      }
    });

    // Group nodes by level and assign columns
    const levels = new Map<number, GraphNode[]>();
    nodes.forEach(node => {
      if (!levels.has(node.level!)) {
        levels.set(node.level!, []);
      }
      levels.get(node.level!)!.push(node);
    });

    // Calculate positions
    const verticalSpacing = 150;
    const horizontalSpacing = 200;
    const padding = 100;

    levels.forEach((levelNodes, level) => {
      const totalWidth = (levelNodes.length - 1) * horizontalSpacing;
      const startX = (this.width - totalWidth) / 2;
      const y = padding + level * verticalSpacing;

      levelNodes.forEach((node, index) => {
        node.column = index;
        node.x = startX + index * horizontalSpacing;
        node.y = y;
      });
    });
  }

  onNodeClick(node: GraphNode): void {
    this.selectedNode.set(this.selectedNode() === node ? null : node);
  }

  onNodeMouseEnter(node: GraphNode): void {
    this.hoveredNode.set(node);
  }

  onNodeMouseLeave(): void {
    this.hoveredNode.set(null);
  }

  getLinksForNode(node: GraphNode): GraphLink[] {
    return this.links().filter(
      link => link.source.id === node.id || link.target.id === node.id
    );
  }

  isLinkHighlighted(link: GraphLink): boolean {
    const selected = this.selectedNode();
    if (!selected) return false;
    return link.source.id === selected.id || link.target.id === selected.id;
  }

  isNodeHighlighted(node: GraphNode): boolean {
    const selected = this.selectedNode();
    if (!selected) return false;
    if (node.id === selected.id) return true;

    return this.links().some(
      link =>
        (link.source.id === selected.id && link.target.id === node.id) ||
        (link.target.id === selected.id && link.source.id === node.id)
    );
  }

  getLinkPath(link: GraphLink): string {
    return `M ${link.source.x},${link.source.y} L ${link.target.x},${link.target.y}`;
  }

  getLinkColor(link: GraphLink): string {
    return this.connectionTypeColors[link.connection.connection.connectionType];
  }

  getProfileInitials(profile: Profile): string {
    const first = profile.firstname?.charAt(0) || '';
    const last = profile.lastname?.charAt(0) || '';
    return (first + last).toUpperCase();
  }

  onEditConnection(connection: ConnectionWithProfiles): void {
    this.editConnection.emit(connection);
  }

  onDeleteConnection(connection: ConnectionWithProfiles): void {
    this.deleteConnection.emit(connection);
  }
}
