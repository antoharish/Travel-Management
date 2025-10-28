import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-add-manager',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-manager.component.html',
  styleUrls: ['./add-manager.component.css'] // <-- should be styleUrls
})
export class AddManagerComponent {
  @Output() close = new EventEmitter<void>();
  name = '';
  username = '';
  email = '';
  password = '';
  contactNumber = '';

  constructor(private authService: AuthService) {}

  addManager() {
    const manager = {
      name: this.name,
      username: this.username,
      email: this.email,
      password: this.password,
      contactNumber: this.contactNumber
    };
    this.authService.registerManager(manager).subscribe({
      next: (res) => {
        // Show backend success message if available
        alert(res?.message || 'Manager added successfully!');
        this.close.emit();
      },
      error: (err) => {
        // Show backend error message if available
        alert(err.error?.message || err.error || 'Failed to add manager!');
      }
    });
  }
}