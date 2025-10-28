import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { FlightService } from '../../services/flight.service';
import { PaymentService } from '../../services/payment.service';
import { AuthService } from '../../services/auth.service';
import { Flight } from '../../models/flight.model';
import { NavBarComponent } from "../../navigation/navigation.component";
import { FooterComponent } from "../../footer/footer.component";

@Component({
  selector: 'app-flight-booking',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NavBarComponent, FooterComponent],
  templateUrl: './flight-booking.component.html',
  styleUrls: ['./flight-booking.component.css']
})
export class FlightBookingComponent implements OnInit, OnDestroy {
  flightId!: number;
  flight?: Flight;
  bookingForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  private destroy$ = new Subject<void>();
  showConfirmationPopup = false;
  bookingDetails: any = {};
  userId!: number;
  minDate: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private flightService: FlightService,
    private paymentService: PaymentService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.initForm();
    this.minDate = new Date().toISOString().split('T')[0];
  }

  private initForm(): void {
    this.bookingForm = this.fb.group({
      departureDate: ['', Validators.required],
      passengers: [1, [Validators.required, Validators.min(1)]],
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{10,12}$/)]],
      specialRequests: ['']
    });
  }

  ngOnInit(): void {
    // Get user ID from JWT token
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login'], { 
        queryParams: { returnUrl: this.router.url } 
      });
      return;
    }

    // Get userId from token
    try {
      const decodedToken = JSON.parse(atob(token.split('.')[1]));
      this.userId = decodedToken.userId;
      console.log('User ID from token:', this.userId);
    } catch (error) {
      console.error('Error decoding token:', error);
      this.router.navigate(['/login']);
      return;
    }

    this.route.params.pipe(takeUntil(this.destroy$)).subscribe(params => {
      this.flightId = +params['id'];
      this.loadFlightDetails();
    });
  }
  onCheckInChange(): void {
    const checkInDate = this.bookingForm.get('checkIn')?.value;
    if (checkInDate) {
      // Set minimum checkout date to the day after check-in
      const nextDay = new Date(checkInDate);
      nextDay.setDate(nextDay.getDate() + 1);
    }
  }

  private loadFlightDetails(): void {
    this.isLoading = true;
    this.flightService.getFlightById(this.flightId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (flight) => {
          this.flight = flight;
          this.isLoading = false;
        },
        error: (error) => {
          this.errorMessage = 'Error loading flight details';
          this.isLoading = false;
          console.error('Error:', error);
        }
      });
  }

  onSubmit(): void {
    if (this.bookingForm.invalid || this.isLoading) {
      this.bookingForm.markAllAsTouched();
      return;
    }

    this.bookingDetails = {
      flightNumber: this.flight?.flightNumber,
      departureCity: this.flight?.departureCity,
      destinationCity: this.flight?.destinationCity,
      email: this.bookingForm.value.email,
      departureDate: this.bookingForm.value.departureDate,
      passengers: this.bookingForm.value.passengers,
      phone: this.bookingForm.value.phone,
      specialRequests: this.bookingForm.value.specialRequests,
      totalPrice: this.calculateTotalPrice()
    };

    this.showConfirmationPopup = true;
  }
  confirmBooking(): void {
    this.isLoading = true;
    this.clearMessages();

    const formattedDate = new Date(this.bookingForm.value.departureDate)
        .toISOString().split('T')[0];

    this.paymentService.bookFlight(
        this.userId,
        this.bookingForm.value.email,
        this.flightId,
        formattedDate,
        this.bookingForm.value.passengers  // this will be sent as quantity
    ).subscribe({
        next: (response) => {
            console.log('Payment Response:', response);
            this.isLoading = false;
            if (response.sessionUrl) {
                window.location.href = response.sessionUrl;
            } else {
                this.errorMessage = 'Payment session URL not received';
            }
        },
        error: (err) => {
            this.isLoading = false;
            this.errorMessage = 'Failed to initiate payment. Please try again.';
            console.error('Payment error:', err);
            this.showConfirmationPopup = false;
        }
    });
}

calculateTotalPrice(): number {
  if (!this.flight?.price) return 0;
  const passengers = Number(this.bookingForm.get('passengers')?.value) || 1;
  if (isNaN(passengers) || passengers < 1) return 0;
  return this.flight.price * passengers;
}
  closePopup(): void {
    this.showConfirmationPopup = false;
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}