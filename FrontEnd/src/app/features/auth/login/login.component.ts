import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
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

  if (!this.usernameOrEmail || !this.password) {
    alert("Enter credentials");
    return;
  }

  this.loading = true;

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

        alert("Verification Code: " + res.code);

        this.showOtpModal = true;

        this.cd.detectChanges();

        return;
      }

      this.loginSuccess(res);
    },

    error: (err) => {

      this.loading = false;
      this.cd.detectChanges();

      alert(
        err?.error?.message ||
        "Invalid credentials"
      );
    }
  });
}

  /* ================= VERIFY OTP ================= */

  verifyOtp() {

    if (!this.enteredOtp) {
      alert("Enter OTP");
      return;
    }

    this.api.verify2FA(
      this.otpUser,
      this.enteredOtp
    ).subscribe({

      next: (res: any) => {

        this.showOtpModal = false;
        this.loginSuccess(res);
      },

      error: () => {
        alert("Invalid OTP");
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