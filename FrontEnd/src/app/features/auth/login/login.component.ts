import { Component, ChangeDetectorRef } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  usernameOrEmail = '';
  password = '';

  showPassword = false;
  loading = false;

  showOtpModal = false;
  enteredOtp = '';
  otpUser = '';

  errorMessage = '';
  successMessage = '';

  constructor(
    private api: ApiService,
    private router: Router,
    private cd: ChangeDetectorRef
  ) {}

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  /* ================= LOGIN ================= */

  login() {

    this.errorMessage = '';
    this.successMessage = '';

    if (!this.usernameOrEmail || !this.password) {
      this.errorMessage = "Enter credentials";
      return;
    }

    this.loading = true;
    this.cd.detectChanges();   // 🔥 force UI refresh

    this.api.login({
      usernameOrEmail: this.usernameOrEmail,
      masterPassword: this.password
    })
    .subscribe({

      next: (res: any) => {

        this.loading = false;
        this.cd.detectChanges();

        if (res.twoFactorRequired) {

          this.otpUser = res.username;
          this.showOtpModal = true;
          this.cd.detectChanges();
          return;
        }

        this.successMessage = "Login successful";
        this.loginSuccess(res);
      },

      error: (err) => {

        this.loading = false;

        this.errorMessage =
          err?.error?.message || "Invalid credentials";

        this.cd.detectChanges();   // 🔥 force Angular refresh

      }
    });
  }

  /* ================= VERIFY OTP ================= */

  verifyOtp() {

    if (!this.enteredOtp) {
      this.errorMessage = "Enter OTP";
      return;
    }

    this.api.verify2FA(this.otpUser, this.enteredOtp)
      .subscribe({

        next: (res: any) => {

          this.showOtpModal = false;
          this.loginSuccess(res);
        },

        error: () => {
          this.errorMessage = "Invalid OTP";
          this.cd.detectChanges();
        }
      });
  }

  /* ================= SUCCESS ================= */

  loginSuccess(res: any) {

    localStorage.setItem('token', res.token);
    localStorage.setItem('username', res.username);
    localStorage.setItem('userId', res.userId);

    this.router.navigate(['/dashboard']);
  }
}