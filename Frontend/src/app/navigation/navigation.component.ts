import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Subscription } from 'rxjs';
import { ProfileComponent } from '../profile/profile.component';

@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [CommonModule, RouterModule, ProfileComponent],
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavBarComponent implements OnInit, OnDestroy {
  isLoggedIn = false;
  username = '';
  isProfilePopupVisible = false; // For toggling the profile popup
  private subscriptions: Subscription[] = [];

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.subscriptions.push(
      this.authService.authState$.subscribe(
        state => this.isLoggedIn = state
      ),
      this.authService.username$.subscribe(
        username => this.username = username
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  toggleProfilePopup() {
    this.isProfilePopupVisible = !this.isProfilePopupVisible;
  }
}