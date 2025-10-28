import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-add-agent',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-agent.component.html',
  styleUrls: ['./add-agent.component.css'] // <-- should be styleUrls
})
export class AddAgentComponent {
  @Output() close = new EventEmitter<void>();
  name = '';
  username = '';
  email = '';
  password = '';
  contactNumber = '';

  constructor(private authService: AuthService) {}

  addAgent() {
    const agent = {
      name: this.name,
      username: this.username,
      email: this.email,
      password: this.password,
      contactNumber: this.contactNumber
    };
    this.authService.registerAgent(agent).subscribe({
      next: (res) => {
        alert(res?.message || 'Travel Agent added successfully!');
        this.close.emit();
      },
      error: (err) => {
        alert(err.error?.message || err.error || 'Failed to add Travel Agent!');
      }
    });
  }
}