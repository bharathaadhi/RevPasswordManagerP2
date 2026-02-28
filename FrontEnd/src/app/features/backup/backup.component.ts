import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-backup',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './backup.component.html',
  styleUrls: ['./backup.component.css']
})
export class BackupComponent {

  exportMasterPassword = '';
  exportVerificationCode = '';

  showExportPassword = false;
  showImportPassword = false;

  importMasterPassword = '';
  importVerificationCode = '';

  selectedFile: File | null = null;

  constructor(private api: ApiService) { }

  // ================= GENERATE CODE =================

  generateCode() {

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

  // ================= EXPORT =================

  exportVault() {

    const user = localStorage.getItem('username');
    if (!user) return;

    this.api.secureExportVault({
      usernameOrEmail: user,
      masterPassword: this.exportMasterPassword,
      verificationCode: this.exportVerificationCode
    })
      .subscribe((blob: Blob) => {

        const url = window.URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = 'rev-vault-backup.json';
        a.click();

        window.URL.revokeObjectURL(url);

        alert("Vault Exported Successfully");
      });
  }

  // ================= IMPORT =================

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  importVault() {

    if (!this.selectedFile) {
      alert("Select backup file");
      return;
    }

    const user = localStorage.getItem('username');
    if (!user) return;

    const reader = new FileReader();

    reader.onload = () => {

      const data = JSON.parse(reader.result as string);

      this.api.secureImportVault({
        usernameOrEmail: user,
        masterPassword: this.importMasterPassword,
        verificationCode: this.importVerificationCode,
        vaultData: data
      }).subscribe({
        next: () => {

          alert("Import Successful");

          window.location.href = "/vault";

        },
        error: () => alert("Import Failed")
      });

    };

    reader.readAsText(this.selectedFile);
  }

  toggleExportPassword() {
    this.showExportPassword = !this.showExportPassword;
  }

  toggleImportPassword() {
    this.showImportPassword = !this.showImportPassword;
  }
}