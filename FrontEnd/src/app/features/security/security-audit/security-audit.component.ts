import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ApiService } from '../../../core/services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-security-audit',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './security-audit.component.html',
  styleUrls: ['./security-audit.component.css']
})
export class SecurityAuditComponent implements OnInit {

  weakPasswords: any[] = [];
  reusedPasswords: any[] = [];
  securityScore: number = 0;
  alertMessage: string = '';
  oldPasswords: any[] = [];

  private platformId = inject(PLATFORM_ID);

  constructor(
    private api: ApiService,
    private router: Router,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    // SSR Protection
    if (!isPlatformBrowser(this.platformId)) return;

    const user = localStorage.getItem('username');
    if (!user) return;

    this.loadAudit(user);
  }

  loadAudit(username: string) {

    // ===== WEAK PASSWORDS =====
    this.api.getWeakPasswords(username).subscribe((res: any) => {

      if (Array.isArray(res)) {
        this.weakPasswords = res;
      } else if (res?.data) {
        this.weakPasswords = res.data;
      } else {
        this.weakPasswords = [];
      }

      this.cd.detectChanges(); 
    });

    // ===== REUSED PASSWORDS =====
    this.api.getReusedPasswords(username).subscribe((res: any) => {

      if (Array.isArray(res)) {
        this.reusedPasswords = res;
      } else if (res?.data) {
        this.reusedPasswords = res.data;
      } else {
        this.reusedPasswords = [];
      }

      this.cd.detectChanges();
    });

    // ===== OLD PASSWORDS =====
    this.api.getOldPasswords(username).subscribe((res: any) => {

      if (Array.isArray(res)) {
        this.oldPasswords = res;
      } else if (res?.data) {
        this.oldPasswords = res.data;
      } else {
        this.oldPasswords = [];
      }

      this.cd.detectChanges();
    });

    // ===== SECURITY REPORT =====
    this.api.getSecurityReport(username).subscribe((res: any) => {

      this.securityScore = res?.securityScore ?? 0;
      this.alertMessage = res?.alertMessage ?? '';

      this.cd.detectChanges();
    });
  }

  // ===== FIX BUTTON =====
  fixWeakPasswords() {
    this.router.navigate(['/vault'], {
      queryParams: { filter: 'weak' }
    });
  }
}