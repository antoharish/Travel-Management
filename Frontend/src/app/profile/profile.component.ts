import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  userEmail: string | null = '';
  userRole: string | null = '';
  userId: number | null = null;

  showChangePassword = false;
passwordMessage = '';
changePasswordForm: FormGroup;
 

  constructor(private authService: AuthService, private fb: FormBuilder) {
    this.changePasswordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.userRole = this.authService.getCurrentUserRole();
    this.userId = this.authService.getCurrentUserId();

    if (this.userId) {
      this.authService.getUserById(this.userId).subscribe({
        next: (user) => {
          console.log('User API response:', user);
          this.userEmail = user?.email || user?.userEmail || user?.mail || 'Not available';
        },
        error: (err) => {
          console.error('User fetch error:', err);
          this.userEmail = 'Not available';
        }
      });
    }
  }
  onChangePassword() {
    if (this.changePasswordForm.invalid) {
      this.passwordMessage = 'Please fill all fields correctly.';
      return;
    }
    if (this.changePasswordForm.value.newPassword !== this.changePasswordForm.value.confirmPassword) {
      this.passwordMessage = 'New passwords do not match.';
      return;
    }
    this.authService.changePassword(
      this.userId!,
      this.changePasswordForm.value.oldPassword,
      this.changePasswordForm.value.newPassword
    ).subscribe({
      next: () => {
        this.passwordMessage = 'Password updated successfully!';
        this.showChangePassword = false;
        this.changePasswordForm.reset();
      },
      error: (err) => {
        if (typeof err.error === 'string') {
          this.passwordMessage = err.error;
        } else if (err.error && err.error.message) {
          this.passwordMessage = err.error.message;
        } else {
          this.passwordMessage = 'Failed to update password.';
        }
      }
    });
 
    console.log(this.changePasswordForm.value, this.changePasswordForm.valid, this.changePasswordForm.errors);
  }
}