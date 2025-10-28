

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap, BehaviorSubject } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
  sub: string;
  userId: number;
  exp: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:9070/api';
  private authStateChange = new BehaviorSubject<boolean>(this.isLoggedIn());
  private usernameSubject = new BehaviorSubject<string>('');

  constructor(private http: HttpClient) {
    this.checkAuthState();
  }

  private checkAuthState() {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decoded = jwtDecode<DecodedToken>(token);
        if (decoded.exp * 1000 > Date.now()) {
          this.authStateChange.next(true);
          this.usernameSubject.next(decoded.sub);
        }
      } catch {
        this.logout();
      }
    }
  }

  get authState$() {
    return this.authStateChange.asObservable();
  }

  get username$() {
    return this.usernameSubject.asObservable();
  }

  private getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    };
  }

  registerAgent(agent: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register/tm`, agent, this.getAuthHeaders());
  }

  registerManager(manager: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register/hm`, manager, this.getAuthHeaders());
  }

  registerUser(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  // login(credentials: any): Observable<any> {
  //   return this.http.post(`${this.apiUrl}/login`, credentials);
  // }
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response.token) {
          localStorage.setItem('token', response.token);
          const decoded = jwtDecode<DecodedToken>(response.token);
          this.authStateChange.next(true);
          this.usernameSubject.next(decoded.sub);
        }
      })
    );
  }
  
  getCurrentUserId(): number | null {
    const token = localStorage.getItem('token');
    if (!token) {
      console.log('No token found in localStorage');
      return null;
    }
  
    try {
      const decoded = jwtDecode<{ userId: number; exp: number }>(token);
      console.log('Decoded Token:', decoded);
      const currentTime = Math.floor(Date.now() / 1000);
      if (decoded.exp > currentTime) {
        return decoded.userId || null; // Extract `userId` directly
      } else {
        console.log('Token is expired');
        localStorage.removeItem('token');
        return null;
      }
    } catch (error) {
      console.error('Failed to decode token:', error);
      return null;
    }
  }

  getCurrentUserEmail(): string | null {
    const token = localStorage.getItem('token');
    if (!token) {
      console.log('No token found in localStorage');
      return null;
    }
 
    try {
      const decoded = jwtDecode<{ email: string; exp: number }>(token);
      console.log('Decoded Token:', decoded);
      const currentTime = Math.floor(Date.now() / 1000);
      if (decoded.exp > currentTime) {
        return decoded.email || null;
      } else {
        console.log('Token is expired');
        localStorage.removeItem('token');
        return null;
      }
    } catch (error) {
      console.error('Failed to decode token:', error);
      return null;
    }
  }
  getCurrentUserRole(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return null;
    try {
      const decoded = jwtDecode<{ role: string, exp: number }>(token);
      const currentTime = Math.floor(Date.now() / 1000);
      if (decoded.exp > currentTime) {
        return decoded.role || null;
      } else {
        localStorage.removeItem('token');
        return null;
      }
    } catch {
      return null;
    }
  }
 
  getUserById(userId: number) {
    return this.http.get<any>(`${this.apiUrl}/${userId}`, this.getAuthHeaders());
  }
  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return false;
  
    try {
      const decoded = jwtDecode<DecodedToken>(token);
      return decoded.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.authStateChange.next(false);
    this.usernameSubject.next('');
  }

  getUsername(): string {
    const token = localStorage.getItem('token');
    if (!token) return '';
    
    try {
      const decoded = jwtDecode<DecodedToken>(token);
      return decoded.sub;
    } catch {
      return '';
    }
  }
  changePassword(userId: number, oldPassword: string, newPassword: string) {
    return this.http.put(
      `${this.apiUrl}/change-password/${userId}`,
      { oldPassword, newPassword },
      this.getAuthHeaders()
    );
  }
}