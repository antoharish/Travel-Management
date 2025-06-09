import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-flight-add',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddFlightComponent {
  flightForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.flightForm = this.fb.group({
      departureCity: ['', Validators.required],
      destinationCity: ['', Validators.required],
      departureTime: ['', Validators.required],
      arrivalTime: ['', Validators.required],
      totalSeats: [0, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(1)]],
      departureDateTime: ['', Validators.required]
    });
  }

  addFlight() {
    if (this.flightForm.valid) {
      this.http.post('http://localhost:9070/flights', this.flightForm.value).subscribe({
        next: () => {
          alert('Flight added successfully!');
          this.flightForm.reset();
        },
        error: (error) => {
          console.error('Error adding flight:', error);
          alert('Failed to add flight.');
        }
      });
    } else {
      alert('Please fill out all required fields.');
    }
  }
}