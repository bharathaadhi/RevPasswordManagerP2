import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {

  step = 1;

  usernameOrEmail = '';

  questions: string[] = [];
  answers: string[] = ['', '', ''];

  newPassword = '';
  confirmPassword = '';

  showPassword = false;
  showConfirmPassword = false;

  constructor(
    private api: ApiService,
    private router: Router
  ) {}

  // ================= STEP 1 =================
  verifyUser() {

    if (!this.usernameOrEmail) {
      alert("Enter username/email");
      return;
    }

    this.api.getSecurityQuestions(this.usernameOrEmail)
      .subscribe({

        next: (q: string[]) => {

          if (!q || q.length === 0) {
            alert("Security questions not configured");
            return;
          }

          this.questions = q.slice(0,3);
          this.step = 2;
        },

        error: () => {
          alert("User not found");
        }
      });
  }

  // ================= STEP 2 =================
  verifyAnswer() {

    const valid =
      this.answers.some(a => a?.trim());

    if (!valid) {
      alert("Answer at least one question");
      return;
    }

    this.step = 3;
  }

  // ================= STEP 3 =================
  resetPassword() {

    if (!this.newPassword || !this.confirmPassword) {
      alert("Enter password");
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      alert("Passwords not matching");
      return;
    }

    const securityQuestions =
      this.questions.map((q,i)=>({
        question:q,
        answer:this.answers[i]
      }))
      .filter(a=>a.answer?.trim());

    this.api.forgotPassword({
        usernameOrEmail:this.usernameOrEmail,
        newPassword:this.newPassword,
        securityQuestions
    })
    .subscribe({

      next:(res:any)=>{

        alert(res.message);

        this.router.navigate(['/login']);
      },

      error:(err)=>{

        alert(
          err?.error?.message ||
          "Password reset failed"
        );
      }
    });
  }

  togglePassword(){
    this.showPassword=!this.showPassword;
  }

  toggleConfirmPassword(){
    this.showConfirmPassword=!this.showConfirmPassword;
  }
}