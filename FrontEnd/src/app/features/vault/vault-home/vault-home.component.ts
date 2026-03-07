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
  allPasswords: any[] = [];

  totalPasswords = 0;
  weakCount = 0;
  favoriteCount = 0;
  securityScore = 0;

  /* ================= TOAST ================= */

  toastMessage = '';
  toastType = '';

  /* ================= FORM ERROR ================= */

  formError = '';
  viewError = '';

  /* ================= MODALS ================= */

  showAdd = false;
  showViewModal = false;
  showEditModal = false;
  showDeleteModal = false;

  isVerifying = false;
  showPassword = false;
  showDeletePassword = false;
  showMasterPassword = false;

  selectedEntryId: number | null = null;
  deleteEntryId: number | null = null;
  editingId: number | null = null;

  masterPasswordInput = '';
  decryptedPassword = '';
  viewVerificationCode = '';

  deleteMasterPassword = '';
  deleteVerificationCode = '';
  deleteError = '';

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

    this.route.queryParams.subscribe(params => {

      const user = localStorage.getItem('username');
      if (!user) return;

      if (params['filter'] === 'weak') {
        this.loadWeakPasswords(user);
      }
      else if (params['filter'] === 'favorite') {
        this.loadFavorites();
      }
      else {
        this.loadVault();
      }

      if (params['generatedPassword']) {
        setTimeout(() => {
          this.openAdd();
          this.newPassword.password = params['generatedPassword'];
        });
      }

    });

  }

  /* ================= TOAST FUNCTION ================= */

  showToast(message: string, type: string) {

    this.toastMessage = message;
    this.toastType = type;

    setTimeout(() => {
      this.toastMessage = '';
    }, 3000);
  }

  /* ================= LOAD VAULT ================= */

  loadVault() {

    const user = localStorage.getItem('username');
    if (!user) return;

    this.api.getVault().subscribe({

      next: (res: any[]) => {

        this.allPasswords = [...res];
        this.passwords = [...res];

        /* update stats */

        this.totalPasswords = this.allPasswords.length;

        this.favoriteCount =
          this.allPasswords.filter(p => p.favorite).length;

        this.cd.detectChanges();

      }

    });

    this.api.getSecurityReport(user).subscribe({

      next: (report: any) => {

        this.weakCount = report.weakPasswords;
        this.securityScore = report.securityScore;

        this.cd.detectChanges();

      }

    });

  }

  /* ================= CATEGORY FILTER ================= */
  onCategoryChange(event: Event) {

    const category = (event.target as HTMLSelectElement).value;

    if (category === 'ALL') {

      this.passwords = [...this.allPasswords];
      return;

    }

    this.passwords = this.allPasswords.filter(p =>
      p.category === category
    );

  }
  /* ================= SORT ================= */

  onSortChange(event: Event) {

    const sortBy = (event.target as HTMLSelectElement).value;

    if (sortBy === 'name') {

      this.passwords = [...this.passwords].sort((a, b) =>
        a.accountName.localeCompare(b.accountName)
      );

    }

    if (sortBy === 'created') {

      this.passwords = [...this.passwords].sort((a, b) =>
        new Date(b.createdAt).getTime() -
        new Date(a.createdAt).getTime()
      );

    }

    if (sortBy === 'updated') {

      this.passwords = [...this.passwords].sort((a, b) =>
        new Date(b.updatedAt).getTime() -
        new Date(a.updatedAt).getTime()
      );

    }

  }

  /* ================= SEARCH ================= */

  search() {

    if (!this.searchKeyword) {

      this.passwords = [...this.allPasswords];
      return;

    }

    const keyword = this.searchKeyword.toLowerCase();

    this.passwords = this.allPasswords.filter(p =>
      p.accountName?.toLowerCase().includes(keyword) ||
      p.website?.toLowerCase().includes(keyword) ||
      p.username?.toLowerCase().includes(keyword)
    );

  }

  /* ================= FAVORITES ================= */

  loadFavorites() {

    this.passwords = this.allPasswords.filter(p => p.favorite);

  }

  /* ================= WEAK PASSWORDS ================= */

  loadWeakPasswords(user: string) {

    this.api.getWeakPasswords(user)
      .subscribe(res => this.passwords = res || []);

  }

  /* ================= ADD PASSWORD ================= */

  openAdd() {

    this.showAdd = true;
    this.formError = '';

    this.newPassword = {
      accountName: '',
      website: '',
      username: '',
      password: '',
      category: 'SOCIAL_MEDIA',
      notes: ''
    };

  }

  closeAdd() {

    this.showAdd = false;
    this.formError = '';
    this.passwordStrength = '';

  }

  savePassword() {

    this.formError = '';

    if (!this.newPassword.accountName ||
      !this.newPassword.username ||
      !this.newPassword.password) {

      this.formError =
        "Account Name, Username and Password are required";

      return;
    }

    const user = localStorage.getItem('username');
    if (!user) return;

    this.api.addPassword({
      usernameOrEmail: user,
      ...this.newPassword
    }).subscribe({

      next: () => {

        this.showAdd = false;

        this.showToast(
          "Password added successfully",
          "toast-success"
        );

        this.loadVault();

      },

      error: () => {

        this.showToast(
          "Failed to add password",
          "toast-error"
        );

      }

    });

  }

  /* ================= DELETE ================= */

  openDelete(id: number) {

    this.deleteEntryId = id;

    this.deleteMasterPassword = '';
    this.deleteVerificationCode = '';
    this.deleteError = '';

    this.showDeleteModal = true;

  }

  confirmDelete() {

    this.deleteError = '';

    if (!this.deleteMasterPassword || !this.deleteVerificationCode) {

      this.deleteError = "Master password and verification code are required";
      return;

    }

    const user = localStorage.getItem('username');
    if (!user || !this.deleteEntryId) return;

    this.api.secureDeletePassword({

      entryId: this.deleteEntryId,
      usernameOrEmail: user,
      masterPassword: this.deleteMasterPassword,
      verificationCode: this.deleteVerificationCode

    }).subscribe({

      next: () => {

        this.showDeleteModal = false;

        this.deleteMasterPassword = '';
        this.deleteVerificationCode = '';
        this.deleteError = '';

        this.showToast(
          "Password deleted successfully",
          "toast-success"
        );

        this.loadVault();

      },

      error: () => {

        this.deleteError = "Invalid master password or verification code";

      }

    });

  }

  /* ================= VIEW PASSWORD ================= */

  openView(id: number) {

    this.selectedEntryId = id;

    this.masterPasswordInput = '';
    this.viewVerificationCode = '';
    this.decryptedPassword = '';
    this.showPassword = false;

    this.showViewModal = true;

  }

  closeView() {

    this.showViewModal = false;

    this.masterPasswordInput = '';
    this.viewVerificationCode = '';
    this.decryptedPassword = '';

  }

  verifyAndView() {

    const user = localStorage.getItem('username');
    if (!user || !this.selectedEntryId) return;

    this.viewError = '';

    /* FRONTEND VALIDATION */

    if (!this.masterPasswordInput || !this.viewVerificationCode) {

      this.viewError = "Master password and verification code required";
      return;

    }

    this.isVerifying = true;

    this.api.viewPassword({

      entryId: this.selectedEntryId,
      usernameOrEmail: user,
      masterPassword: this.masterPasswordInput,
      verificationCode: this.viewVerificationCode

    })
      .pipe(finalize(() => this.isVerifying = false))
      .subscribe({

        next: (res: any) => {

          if (res?.decryptedPassword) {

            this.decryptedPassword = res.decryptedPassword;
            this.showPassword = true;

            this.cd.detectChanges();

          }

        },

        error: () => {

          this.viewError = "Invalid master password or verification code";

        }

      });

  }

  /* ================= GENERATE CODE ================= */

  generateViewCode() {

    const user = localStorage.getItem('username');
    if (!user) return;

    this.api.generateVerificationCode(user)
      .subscribe({

        next: (res: any) => {

          alert(`
📧 REV PASSWORD MANAGER EMAIL

To: ${res.email}

Verification Code: ${res.code}

Expires in 5 minutes.
`);

        }

      });

  }

  /* ================= EDIT ================= */

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

    }).subscribe({

      next: () => {

        this.showEditModal = false;

        this.showToast(
          "Password updated successfully",
          "toast-success"
        );

        this.loadVault();

      },

      error: () => {

        this.showToast(
          "Update failed",
          "toast-error"
        );

      }

    });

  }

  /* ================= PASSWORD STRENGTH ================= */

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

  togglePassword() {
    this.showMasterPassword = !this.showMasterPassword;
  }

  toggleFavorite(p: any) {

    const newValue = !p.favorite;

    this.api.favoritePassword(p.id, newValue)
      .subscribe({

        next: () => {

          const index = this.allPasswords.findIndex(x => x.id === p.id);

          if (index !== -1) {
            this.allPasswords[index].favorite = newValue;
          }

          this.passwords = [...this.allPasswords];
          
          this.favoriteCount =
            this.allPasswords.filter(x => x.favorite).length;

          this.showToast(
            "Favorite updated",
            "toast-success"
          );

        },

        error: () => {

          this.showToast(
            "Favorite update failed",
            "toast-error"
          );

        }

      });

  }

  toggleDeletePassword() {
    this.showDeletePassword = !this.showDeletePassword;
  }

}