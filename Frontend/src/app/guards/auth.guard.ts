import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const expectedRoles = route.data['roles'] as string[];
    const isLoggedIn = this.authService.isLoggedIn();
    const userRole = this.authService.getCurrentUserRole();

    if (!isLoggedIn) {
      this.router.navigate(['/login']);
      return false;
    }

    // if (expectedRoles && !expectedRoles.includes(userRole!)) {
    //   // Optionally redirect to unauthorized page or home
    //   this.router.navigate(['/']);
    //   return false;
    // }
    if (expectedRoles && !expectedRoles.includes(userRole!)) {
      this.router.navigate(['/unauthorized']);
      return false;
    }

    return true;
  }
}