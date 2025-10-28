import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [],
  templateUrl: './unauthorized.component.html',
  styleUrls: ['./unauthorized.component.css']
})
export class UnauthorizedComponent {
  constructor(private authService: AuthService, private router: Router) {}

  goToHomeOrDashboard() {
    const role = this.authService.getCurrentUserRole();
    if (role === 'ADMIN') {
      this.router.navigate(['/admin-dashboard']);
    } else if (role === 'HOTELMANAGER') {
      this.router.navigate(['/manager-dashboard']);
    } else if (role === 'TRAVELAGENT') {
      this.router.navigate(['/travel-agent-dashboard']);
    } else {
      this.router.navigate(['/Home']);
    }
  }
}
