import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  /* ================= STEP FLOW ================= */

  step = 1;

  showPassword = false;

  securityQuestionOptions = [
    'What is your first school name?',
    'What is your mother’s maiden name?',
    'What is your favorite movie?',
    'What was your childhood nickname?',
    'What is your birth city?',
    'What is your favorite food?'
  ];

  registerData = {
    username: '',
    email: '',
    phone: '',
    masterPassword: '',
    securityQuestions: [
      { question: '', answer: '' },
      { question: '', answer: '' },
      { question: '', answer: '' }
    ]
  };

  constructor(
    private api: ApiService,
    private router: Router
  ) { }

  /* ================= UI FUNCTIONS ================= */

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  /* ===== SAFE LOGIN NAVIGATION ===== */

  goToLogin(): void {
    this.router.navigateByUrl('/login'); // more reliable than navigate()
  }

  /* ================= STEP CONTROL ================= */

  nextStep(): void {

    const { username, email, masterPassword } = this.registerData;

    if (!username?.trim() || !email?.trim() || !masterPassword?.trim()) {
      alert('Please fill all required fields');
      return;
    }

    this.step = 2;
  }

  previousStep(): void {
    this.step = 1;
  }

  /* ================= REGISTER ================= */

  register(): void {

    // Check empty questions
    const hasEmpty = this.registerData.securityQuestions.some(
      q => !q.question || !q.answer?.trim()
    );

    if (hasEmpty) {
      alert('Please complete all security questions');
      return;
    }

    // Check duplicates
    const selectedQuestions =
      this.registerData.securityQuestions.map(q => q.question);

    const hasDuplicate =
      new Set(selectedQuestions).size !== selectedQuestions.length;

    if (hasDuplicate) {
      alert('Please select different security questions');
      return;
    }

    this.api.register(this.registerData).subscribe({

      next: (res: any) => {

        alert(res.message);

          this.router.navigate(['/login']);
      },

      error: (err) => {
        alert(err.error.message);
      }

    });
  }
}