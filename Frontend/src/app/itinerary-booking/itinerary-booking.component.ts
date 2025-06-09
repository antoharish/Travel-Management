import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-itinerary-booking',
  templateUrl: './itinerary-booking.component.html',
  imports: [ReactiveFormsModule],
  styleUrls: ['./itinerary-booking.component.css']
})
export class ItineraryBookingComponent implements OnInit {
  bookingForm: FormGroup;
  hotelPrice: number = 0;
  flightPrice: number = 0;
  activitiesPrice: number = 0;
  totalPrice: number = 0;
  isLoading: boolean = false;
  itineraryId: number = 0; // Store the itinerary ID

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router,
    private http: HttpClient,
    private authService: AuthService
  ) {
    this.bookingForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.itineraryId = +params['itineraryId'] || 0; // Retrieve itineraryId
      this.hotelPrice = +params['hotelPrice'] || 0;
      this.flightPrice = +params['flightPrice'] || 0;
      this.activitiesPrice = +params['activitiesPrice'] || 0;
      this.totalPrice = +params['totalPrice'] || 0;

      console.log('Itinerary ID:', this.itineraryId); // Debugging log
    });
  }

  proceedToPayment(): void {
    console.log('Proceeding to payment...');
    const itineraryId = this.itineraryId; // Use the itineraryId from query params
    console.log(itineraryId);

    if (!itineraryId) {
      console.error('Itinerary ID is missing.');
      return;
    }

    const userId = this.authService.getCurrentUserId(); // Get the current user ID dynamically
    const email = "abisheakjagadish01@gmail.com"; // Get the current user email dynamically

    if (!userId) {
      console.error('User ID is missing or user is not logged in.');
      return;
    }
    console.log(email);
    if (!email) {
      console.error('User email is missing or token is invalid.');
      return;
    }

    // Dynamically retrieve booking date and quantity
    const bookingDate = new Date().toISOString().split('T')[0]; // Current date in YYYY-MM-DD format
    const quantity = 1; // Replace with the actual quantity if applicable

    // Construct the URL with query parameters
    const url = `http://localhost:9070/api/payments/package?userId=${userId}&email=${email}&itineraryId=${itineraryId}&bookingDate=${bookingDate}&quantity=${quantity}`;

    console.log('Constructed URL:', url); // Debugging log

    // Call the backend to book the itinerary and create a Stripe payment session
    this.http.post<{ status: boolean; message: string; sessionId: string; sessionUrl: string }>(url, {}).subscribe(
      (response) => {
        console.log('Stripe payment session created:', response);
        const sessionUrl = response.sessionUrl; // Extract the session URL
        if (sessionUrl) {
          window.location.href = sessionUrl; // Redirect to Stripe payment page
        } else {
          console.error('Session URL is missing in the response.');
        }
      },
      (error) => {
        console.error('Error creating payment session:', error);
      }
    );
  }

  onSubmit(): void {
    if (this.bookingForm.valid) {
      this.isLoading = true;
      console.log('Booking Form Submitted:', this.bookingForm.value);

      // Call proceedToPayment after form submission
      this.proceedToPayment();
    }
  }
}
