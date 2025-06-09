import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-activity-add',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule], // Import ReactiveFormsModule for reactive forms
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddActivityComponent {
  activityForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    // Initialize the form with FormBuilder
    this.activityForm = this.fb.group({
      name: ['', Validators.required],
      location: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]]
      
    });
  }

  addActivity() {
    if (this.activityForm.valid) {
      this.http.post('http://localhost:9070/activities', this.activityForm.value).subscribe({
        next: () => {
          alert('Activity added successfully!');
          this.activityForm.reset(); // Reset the form after successful submission
        },
        error: (error) => {
          console.error('Error adding activity:', error);
          alert('Failed to add activity.');
        }
      });
    } else {
      alert('Please fill out all required fields.');
    }
  }
}