import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';


@Component({
  selector: 'app-vault-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vault-home.component.html',
  styleUrls: ['./vault-home.component.css']
})
export class VaultHomeComponent implements OnInit {

  passwords: any[] = [];

  totalPasswords = 0;
  weakCount = 0;
  favoriteCount = 0;
  securityScore = 0;

  showAdd = false;
  showViewModal = false;
  showEditModal = false;
  isVerifying = false;
  showPassword = false;
  showMasterPassword = false;

  selectedEntryId: number | null = null;
  masterPasswordInput = '';
  masterPassword: string = '';
  decryptedPassword = '';
  viewVerificationCode = '';

  showDeleteModal = false;
  deleteEntryId: number | null = null;

  deleteMasterPassword = '';
  deleteVerificationCode = '';

  searchKeyword = '';

  passwordStrength = '';

  categories = [
    'ALL',
    'SOCIAL_MEDIA',
    'BANKING',
    'EMAIL',
    'SHOPPING',
    'WORK',
    'OTHER'
  ];

  newPassword: any = {
    accountName: '',
    website: '',
    username: '',
    password: '',
    category: 'SOCIAL_MEDIA',
    notes: ''
  };

  editingId: number | null = null;

  editPassword: any = {
    accountName: '',
    website: '',
    username: '',
    password: '',
    category: 'SOCIAL_MEDIA',
    notes: ''
  };

  private platformId = inject(PLATFORM_ID);

  constructor(
    private api: ApiService,
    private route: ActivatedRoute,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit() {

    if (!isPlatformBrowser(this.platformId)) return;

    const user = localStorage.getItem('username');
    if (!user) return;

    this.loadVault();

    this.route.queryParams.subscribe(params => {
      if (params['filter'] === 'weak') {
        this.loadWeakPasswords(user);
      }
      else if (params['filter'] === 'favorite') {
        this.loadFavorites();
      }
    });

    const generated = localStorage.getItem('generatedPassword');
    if (generated) {
      this.newPassword.password = generated;
      this.showAdd = true;
      localStorage.removeItem('generatedPassword');
    }
  }

  // ================= LOAD =================

  loadVault() {

    const user = localStorage.getItem('username');
    if (!user) return;

    this.api.getVault().subscribe({
      next: (res: any[]) => {

        console.log("Vault loaded:", res);

        this.passwords = [...res];
        this.totalPasswords = res.length;
        this.favoriteCount = res.filter(p => p.favorite).length;

        this.cd.detectChanges();
      },
      error: () => alert('Failed to load vault')
    });

    this.api.getSecurityReport(user).subscribe({
      next: (report: any) => {
        this.weakCount = report.weakPasswords;
        this.securityScore = report.securityScore;

        this.cd.detectChanges();
      }
    });
  }

  // ================= SORT =================

  onSortChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.sort(selectElement.value);
  }

  // ================= ADD =================

  openAdd() { this.showAdd = true; }

  closeAdd() {
    this.showAdd = false;
    this.passwordStrength = '';
  }

  savePassword() {

    const user = localStorage.getItem('username');
    if (!user) {
      alert('User not found');
      return;
    }

    this.api.addPassword({
      usernameOrEmail: user,
      ...this.newPassword
    }).subscribe({
      next: () => {
        this.showAdd = false;
        this.newPassword = {
          accountName: '',
          website: '',
          username: '',
          password: '',
          category: 'SOCIAL_MEDIA',
          notes: ''
        };
        this.passwordStrength = '';
        this.loadVault();
      },
      error: () => alert('Failed to add password')
    });
  }

  // ================= DELETE =================
  openDelete(id: number) {
    this.deleteEntryId = id;
    this.showDeleteModal = true;
  }

  generateViewCode() {

    const user = localStorage.getItem('username');
    if (!user) return;

    this.api.generateVerificationCode(user)
      .subscribe(res => {

        alert(`
📧 REV PASSWORD MANAGER EMAIL

To: ${res.email}

Your verification code is:
👉 ${res.code}

This code expires in 5 minutes.
`);

      });
  }

  confirmDelete() {

    const user = localStorage.getItem('username');
    if (!user || !this.deleteEntryId) return;

    this.api.secureDeletePassword({
      entryId: this.deleteEntryId,
      usernameOrEmail: user,
      masterPassword: this.deleteMasterPassword,
      verificationCode: this.deleteVerificationCode
    }).subscribe({
      next: () => {
        alert("Password Deleted");
        this.showDeleteModal = false;
        this.loadVault();
      },
      error: () => alert("Delete Failed")
    });
  }

  // ================= FAVORITE =================

  // ================= FAVORITE =================

  toggleFavorite(p: any) {

    this.api.favoritePassword(p.id, !p.favorite)
      .subscribe({
        next: () => {

          this.loadVault();

        },
        error: () => alert("Failed to update favorite")
      });

  }

  // ================= SEARCH =================

  search() {

    const user = localStorage.getItem('username');
    if (!user || !this.searchKeyword) return;

    this.api.searchVault(user, this.searchKeyword)
      .subscribe(res => this.passwords = res);
  }

  // ================= FILTER =================

  onCategoryChange(event: Event) {

    const selectElement = event.target as HTMLSelectElement;
    const category = selectElement.value;
    const user = localStorage.getItem('username');

    if (!user) return;

    if (category === 'ALL') {
      this.loadVault();
      return;
    }

    this.api.filterVault(user, category)
      .subscribe(res => this.passwords = res);
  }

  // ================= SORT =================

  sort(sortBy: string) {

    const user = localStorage.getItem('username');
    if (!user) return;

    this.api.sortVault(user, sortBy)
      .subscribe(res => this.passwords = res);
  }

  // ================= VIEW =================

  openView(id: number) {

    this.selectedEntryId = id;

    this.masterPasswordInput = '';
    this.viewVerificationCode = '';
    this.decryptedPassword = '';

    this.showPassword = false;
    this.isVerifying = false;

    this.showViewModal = true;
  }

  closeView() {

    this.showViewModal = false;

    this.masterPasswordInput = '';
    this.viewVerificationCode = '';
    this.decryptedPassword = '';

    this.selectedEntryId = null;
    this.showPassword = false;
  }

  verifyAndView() {

    const user = localStorage.getItem('username');
    if (!user || !this.selectedEntryId) return;

    this.isVerifying = true;

    this.api.viewPassword({
      entryId: this.selectedEntryId,
      usernameOrEmail: user,
      masterPassword: this.masterPasswordInput,
      verificationCode: this.viewVerificationCode
    })
      .pipe(
        finalize(() => {
          // ALWAYS STOP LOADER
          this.isVerifying = false;
          this.cd.detectChanges();
        })
      )
      .subscribe({

        next: (res: any) => {

          console.log("VIEW RESPONSE:", res);

          if (res && res.decryptedPassword) {

            this.decryptedPassword = res.decryptedPassword;
            this.showPassword = true;

          } else {
            alert("Password not received");
          }

          this.viewVerificationCode = '';
        },

        error: (err) => {

          console.error("VERIFY ERROR:", err);
          alert(err?.error?.message || "Verification Failed");

        }

      });
  }

  // ================= EDIT =================

  openEdit(p: any) {

    this.editingId = p.id;

    this.editPassword = {
      accountName: p.accountName,
      website: p.website,
      username: p.username,
      password: '',
      category: p.category,
      notes: p.notes || ''
    };

    this.showEditModal = true;
  }

  updatePassword() {

    if (!this.editingId) return;

    const user = localStorage.getItem('username');

    this.api.updatePassword(this.editingId, {
      usernameOrEmail: user,
      ...this.editPassword
    }).subscribe(() => {
      this.showEditModal = false;
      this.loadVault();
    });
  }

  // ================= STRENGTH =================

  checkStrength(password: string) {

    let score = 0;

    if (!password) {
      this.passwordStrength = '';
      return;
    }

    if (password.length >= 8) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[a-z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;

    if (score <= 2) this.passwordStrength = 'Weak';
    else if (score <= 4) this.passwordStrength = 'Medium';
    else this.passwordStrength = 'Strong';
  }

  loadFavorites() {

    const user = localStorage.getItem('username');
    if (!user) return;

    this.api.getFavorites()
      .subscribe({
        next: (res: any[]) => {

          this.passwords = res;

          this.favoriteCount = res.length;

          this.cd.detectChanges();
        },
        error: () => alert("Failed to load favorites")
      });

  }

  loadWeakPasswords(user: string) {

    this.api.getWeakPasswords(user).subscribe({
      next: (res: any[]) => {
        this.passwords = res || [];
      },
      error: () => alert('Failed to load weak passwords')
    });

    this.api.getSecurityReport(user).subscribe({
      next: (report: any) => {
        this.totalPasswords = report.totalPasswords;
        this.weakCount = report.weakPasswords;
        this.securityScore = report.securityScore;
      }
    });
  }

  togglePassword() {
    this.showMasterPassword = !this.showMasterPassword;
  }
}