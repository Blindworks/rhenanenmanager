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
  vx: number;
  vy: number;
  fixed: boolean;
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
  private centerX = this.width / 2;
  private centerY = this.height / 2;
  private isDragging = false;
  private draggedNode: GraphNode | null = null;
  private animationFrameId: number | null = null;

  ngOnInit(): void {
    this.buildGraph();
    this.startSimulation();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['connections'] && !changes['connections'].firstChange) {
      this.buildGraph();
    }
  }

  ngOnDestroy(): void {
    this.stopSimulation();
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
      x: this.centerX + (Math.random() - 0.5) * 400,
      y: this.centerY + (Math.random() - 0.5) * 400,
      vx: 0,
      vy: 0,
      fixed: false
    };
  }

  private layoutNodes(): void {
    const nodes = this.nodes();
    if (nodes.length === 0) return;

    const radius = Math.min(this.width, this.height) * 0.35;
    const angleStep = (2 * Math.PI) / nodes.length;

    nodes.forEach((node, i) => {
      if (!node.fixed) {
        node.x = this.centerX + Math.cos(i * angleStep) * radius;
        node.y = this.centerY + Math.sin(i * angleStep) * radius;
      }
    });
  }

  private startSimulation(): void {
    const simulate = () => {
      this.applyForces();
      this.animationFrameId = requestAnimationFrame(simulate);
    };
    this.animationFrameId = requestAnimationFrame(simulate);
  }

  private stopSimulation(): void {
    if (this.animationFrameId !== null) {
      cancelAnimationFrame(this.animationFrameId);
    }
  }

  private applyForces(): void {
    const nodes = this.nodes();
    const links = this.links();

    const alpha = 0.1;
    const linkDistance = 150;
    const linkStrength = 0.3;
    const chargeStrength = -300;
    const centerStrength = 0.05;

    nodes.forEach(node => {
      if (!node.fixed) {
        node.vx *= 0.9;
        node.vy *= 0.9;

        const dx = this.centerX - node.x;
        const dy = this.centerY - node.y;
        node.vx += dx * centerStrength;
        node.vy += dy * centerStrength;
      }
    });

    nodes.forEach((a, i) => {
      nodes.forEach((b, j) => {
        if (i >= j) return;

        const dx = b.x - a.x;
        const dy = b.y - a.y;
        const distance = Math.sqrt(dx * dx + dy * dy) || 1;
        const force = chargeStrength / (distance * distance);

        if (!a.fixed) {
          a.vx -= (dx / distance) * force;
          a.vy -= (dy / distance) * force;
        }
        if (!b.fixed) {
          b.vx += (dx / distance) * force;
          b.vy += (dy / distance) * force;
        }
      });
    });

    links.forEach(link => {
      const dx = link.target.x - link.source.x;
      const dy = link.target.y - link.source.y;
      const distance = Math.sqrt(dx * dx + dy * dy) || 1;
      const force = (distance - linkDistance) * linkStrength;

      if (!link.source.fixed) {
        link.source.vx += (dx / distance) * force;
        link.source.vy += (dy / distance) * force;
      }
      if (!link.target.fixed) {
        link.target.vx -= (dx / distance) * force;
        link.target.vy -= (dy / distance) * force;
      }
    });

    nodes.forEach(node => {
      if (!node.fixed) {
        node.x += node.vx * alpha;
        node.y += node.vy * alpha;

        const padding = 60;
        node.x = Math.max(padding, Math.min(this.width - padding, node.x));
        node.y = Math.max(padding, Math.min(this.height - padding, node.y));
      }
    });
  }

  onNodeMouseDown(event: MouseEvent, node: GraphNode): void {
    event.preventDefault();
    this.isDragging = true;
    this.draggedNode = node;
    node.fixed = true;
  }

  onGraphMouseMove(event: MouseEvent): void {
    if (this.isDragging && this.draggedNode && this.graphContainer) {
      const rect = this.graphContainer.nativeElement.getBoundingClientRect();
      this.draggedNode.x = event.clientX - rect.left;
      this.draggedNode.y = event.clientY - rect.top;
    }
  }

  onGraphMouseUp(): void {
    if (this.draggedNode) {
      this.draggedNode.fixed = false;
      this.draggedNode = null;
    }
    this.isDragging = false;
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
