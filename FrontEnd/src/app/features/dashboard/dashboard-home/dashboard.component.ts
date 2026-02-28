import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router, NavigationEnd, RouterModule } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',

  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  totalPasswords: number = 0;
  weakPasswords: number = 0;
  message: string = '';
  recentEntries: any[] = [];
  favoritePasswords: any[] = [];
  loading: boolean = false;
  securityScore: number = 0;
  reusedPasswords: number = 0;
  oldPasswords: number = 0;

  private platformId = inject(PLATFORM_ID);

  constructor(
    private api: ApiService,
    private router: Router,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    if (!isPlatformBrowser(this.platformId)) return;

    this.loadDashboard();

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.loadDashboard();
      });
  }

  loadDashboard(): void {

    const user = localStorage.getItem('username');
    if (!user) return;

    this.loading = true;

    this.api.dashboard().subscribe({
      next: (res: any) => {

        this.totalPasswords = res?.totalPasswords ?? 0;
        this.weakPasswords = res?.weakPasswords ?? 0;
        this.message = res?.alertMessage ?? '';
        this.recentEntries = res?.recentEntries ?? [];
        this.securityScore = res?.securityScore ?? 0;
        this.reusedPasswords = res?.reusedPasswords ?? 0;
        this.oldPasswords = res?.oldPasswords ?? 0;

        this.favoritePasswords = res?.favoritePasswords ?? [];

        this.loading = false;
        this.cd.detectChanges();
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  viewPassword(id: number) {
    this.router.navigate(['/vault'], {
      queryParams: { highlight: id }
    });
  }

}