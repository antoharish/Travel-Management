import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { FlightService } from '../services/flight.service';
import { Flight } from '../models/flight.model';
import { FlightAvailability } from '../models/flightAvailability.model';

@Component({
    selector: 'app-flights',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterModule ],
    templateUrl: './flights.component.html',
    styleUrls: ['./flights.component.css']
})
export class FlightsComponent implements OnInit {
    searchForm!: FormGroup;
    allFlights: Flight[] = [];
    filteredFlights: FlightAvailability[] | null = null;
    isLoading = false;
    errorMessage = '';
    minDate: string;
    constructor(
        private fb: FormBuilder,
        private flightService: FlightService,
        private router: Router
    ) {
        this.minDate = new Date().toISOString().split('T')[0];
        this.initForm();
    }

    private initForm(): void {
        this.searchForm = this.fb.group({
            from: ['', Validators.required],
            to: ['', Validators.required],
            departureDate: ['', Validators.required],
            travelers: ['1 Adult']
        });
    }

    ngOnInit(): void {
        this.loadAllFlights();
    }
    onCheckInChange(): void {
        const checkInDate = this.searchForm.get('checkIn')?.value;
        if (checkInDate) {
          // Set minimum checkout date to the day after check-in
          const nextDay = new Date(checkInDate);
          nextDay.setDate(nextDay.getDate() + 1);
        }
      }
    

    private loadAllFlights(): void {
        this.isLoading = true;
        this.flightService.getAllFlights().subscribe({
            next: (flights) => {
                this.allFlights = flights;
                this.isLoading = false;
            },
            error: (error) => {
                this.errorMessage = 'Error loading flights';
                this.isLoading = false;
                console.error('Error:', error);
            }
        });
    }

    onSearch(): void {
        if (this.searchForm.valid) {
            const { from, to, departureDate } = this.searchForm.value;
            this.isLoading = true;
            this.flightService.searchFlights(from, to, departureDate).subscribe({
                next: (availabilities) => {
                    this.filteredFlights = availabilities;
                    this.isLoading = false;
                },
                error: (error) => {
                    this.errorMessage = 'Error searching flights';
                    this.isLoading = false;
                    console.error('Error:', error);
                }
            });
        }
    }

    bookFlight(flightId: number): void {
        if (flightId) {
            this.router.navigate(['/flight-booking', flightId]);
            console.log('Booking flight with ID:', flightId);
        }
    }
    navigateToBooking(flightId: number): void {
        console.log('Navigating to booking for flight:', flightId);
        this.router.navigate(['/flight-booking', flightId]);
    }
}