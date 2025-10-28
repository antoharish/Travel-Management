import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { HttpClient, HttpParams } from '@angular/common/http';
import { FlightService } from '../../../../../services/flight.service';



@Component({
  selector: 'app-add-flight-to-package',
  templateUrl: './add-flight-to-package.component.html',
  imports: [
    FormsModule,
    CommonModule
  ],
  styleUrls: ['./add-flight-to-package.component.css']
})
export class AddFlightToPackageComponent implements OnInit {
  from: string = '';
  to: string = '';
  departureDate: string = '';
  travelers: string = '1 Adult';
  availableFlights: any[] = [];
  selectedFlights: any[] = [];
  isLoading = false;
  errorMessage = '';

  constructor(private flightService: FlightService, private http: HttpClient) {}

  ngOnInit(): void {}

  onSearch(): void {
    if (!this.from || !this.to || !this.departureDate) {
      alert('Please fill in all the required fields.');
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Use the `searchFlights` method from the FlightService
    this.flightService.searchFlights(this.from, this.to, this.departureDate).subscribe({
      next: (flights) => {
        console.log('Flights fetched:', flights); // Log the response
        this.availableFlights = flights; // Populate the availableFlights array
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to fetch flight availability. Please try again.';
        this.isLoading = false;
        console.error('Error fetching flights:', err);
      }
    });
  }

  addToPackage(flightWrapper: any): void {
    if (!flightWrapper.packageId) {
      alert('Please enter a Package ID.');
      return;
    }

    const packageId = flightWrapper.packageId;
    const flightId = flightWrapper.flight.id;
    const departureDate = flightWrapper.date;

    const apiUrl = `http://localhost:9070/packages/addFlightToPackage`;
    const params = new HttpParams()
      .set('packageId', packageId)
      .set('flightId', flightId)
      .set('date', departureDate);
    console.log( flightId, departureDate);
    this.http.post(apiUrl, {}, { params }).subscribe({
      next: () => {
        alert(`Flight "${flightId}" added to package ID ${packageId} successfully!`);
        this.selectedFlights.push(flightWrapper.flight, packageId ); // Add flight to the selected list
      },
      error: (err: any) => {
        alert('Failed to add flight to the package.');
        console.error('Error adding flight:', err);
      }
    });
  }

  removeFromPackage(flight: any): void {
    this.selectedFlights = this.selectedFlights.filter(f => f !== flight);
  }

  savePackage(): void {
    console.log('Selected Flights:', this.selectedFlights);
    alert('Flights added to the package successfully!');
  }
}
