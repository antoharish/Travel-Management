import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule,RouterModule,CommonModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'] // <-- should be styleUrls (array)
})
export class SignupComponent {
  username = '';
  email = '';
  password = '';
  name = '';
  contactNumber = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSignup() {
    const user = {
      username: this.username,
      email: this.email,
      password: this.password,
      name: this.name,
      contactNumber: this.contactNumber
    };
    this.authService.registerUser(user).subscribe({
      next: (res) => {
        alert(res?.message || 'User registered successfully!');
        // Optionally redirect to login
      },
      error: (err) => {
        alert(err.error?.message || err.error || 'Registration failed!');
      }
    });
  }
}