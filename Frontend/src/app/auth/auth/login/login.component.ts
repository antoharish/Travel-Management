
// import { Component } from '@angular/core';
// import { Router, RouterModule } from '@angular/router';

// import { jwtDecode } from 'jwt-decode';
// import { FormsModule } from '@angular/forms';
// import { AuthService } from '../../../services/auth.service';
// import { HttpClient, HttpClientModule } from '@angular/common/http';


// @Component({
//   selector: 'app-login',
//   standalone: true,
//   imports: [FormsModule,RouterModule, HttpClientModule],
//   templateUrl: './login.component.html',
//   styleUrls: ['./login.component.css'] // <-- should be styleUrls (array)
// })

// export class LoginComponent {
//   username = '';
//   password = '';

//   constructor(private authService: AuthService, private router: Router) {}

//   onLogin() {
//     const credentials = { username: this.username, password: this.password };
//     this.authService.login(credentials).subscribe({
//       next: (res) => {
//         localStorage.setItem('token', res.token);

//         // Decode token to get role
//         const decoded: any = jwtDecode(res.token);
//         const role = decoded.role || decoded.roles || decoded.authorities;

//         // Redirect based on role
//         if (role === 'ADMIN') {
//           this.router.navigate(['/admin-dashboard']);
//         } else if (role === 'HOTELMANAGER') {
//           this.router.navigate(['/manager-dashboard']);
//         } else if (role === 'TRAVELAGENT') {
//           this.router.navigate(['/agent-dashboard']);
//         } else if (role === 'USER') {
//           this.router.navigate(['/Home']);
//         } else {
//           alert('Unknown role!');
//         }
//       },
//       error: () => {
//         alert('Login failed!');
//       }
//     });
//   }
// }
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../../services/auth.service';
import { jwtDecode } from 'jwt-decode';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule, HttpClientModule,CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    const credentials = { username: this.username, password: this.password };
    this.authService.login(credentials).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        this.errorMessage='';

        // Decode token to get role
        const decoded: any = jwtDecode(res.token);
        const role = decoded.role || decoded.roles || decoded.authorities;

        // Redirect based on role
        switch (role) {
          case 'ADMIN':
            this.router.navigate(['/admin-dashboard']);
            break;
          case 'HOTELMANAGER':
            this.router.navigate(['/manager-dashboard']);
            break;
          case 'TRAVELAGENT':
            this.router.navigate(['/travel-agent-dashboard']);
            break;
          case 'USER':
            this.router.navigate(['/']); // for normal users
            break;
          default:
            this.errorMessage = 'Unknown role!';
            this.router.navigate(['/']);
        }
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Login failed! Please check your credentials.';
      }
    });
  }
}