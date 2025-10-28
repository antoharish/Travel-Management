import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-manage-flights',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css']
})
export class ManageFlightComponent implements OnInit {
  flights: any[] = [];
  editForm: FormGroup;
  editingFlightId: number | null = null;

  constructor(private http: HttpClient, private fb: FormBuilder) {
    this.editForm = this.fb.group({
      departureCity: ['', Validators.required],
      destinationCity: ['', Validators.required],
      departureTime: ['', Validators.required],
      arrivalTime: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit() {
    this.fetchFlights();
  }

  fetchFlights() {
    this.http.get<any[]>('http://localhost:9070/flights').subscribe({
      next: (data) => {
        this.flights = data;
      },
      error: (error) => {
        console.error('Error fetching flights:', error);
        alert('Failed to fetch flights.');
      }
    });
  }

  deleteFlight(flightId: number): void {
    this.http.delete(`http://localhost:9070/flights/${flightId}`).subscribe({
      next: () => {
        alert('Flight deleted successfully!');
        this.fetchFlights();
      },
      error: (error) => {
        console.error('Error deleting flight:', error);
        alert('Failed to delete flight.');
      }
    });
  }

  editFlight(flight: any): void {
    console.log('Editing flight:', flight); // Debugging log
    this.editingFlightId = flight.id; // Ensure this matches your backend model
    this.editForm.patchValue({
      departureCity: flight.departureCity,
      destinationCity: flight.destinationCity,
      departureTime: flight.departureTime,
      arrivalTime: flight.arrivalTime,
      price: flight.price
    });
  }

  updateFlight(): void {
    if (this.editForm.valid && this.editingFlightId !== null) {
      this.http.put(`http://localhost:9070/flights/${this.editingFlightId}`, this.editForm.value).subscribe({
        next: () => {
          alert('Flight updated successfully!');
          this.editingFlightId = null; // Clear the editing state
          this.fetchFlights(); // Refresh the list
        },
        error: (error) => {
          console.error('Error updating flight:', error);
          alert('Failed to update flight.');
        }
      });
    }
  }

  cancelEdit() {
    this.editingFlightId = null;
    this.editForm.reset();
  }
}