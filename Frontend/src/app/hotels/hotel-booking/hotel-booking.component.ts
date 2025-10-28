
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotelService } from '../../services/hotel.service';
import { Hotel } from '../../models/hotel.model';
import { ReviewService } from '../../services/review.service';
import { ReviewByHotelComponent } from '../../review/components/review-by-hotel/review-by-hotel.component';
import { Subject, takeUntil } from 'rxjs';
import { PaymentService } from '../../services/payment.service';
import { AuthService } from '../../services/auth.service';
import { HotelAvailability } from '../../models/hotelAvailability.model';
import { NavBarComponent } from '../../navigation/navigation.component';
import { FooterComponent } from "../../footer/footer.component";

@Component({
  selector: 'app-hotel-booking',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ReviewByHotelComponent, NavBarComponent, RouterModule, FooterComponent],
  templateUrl: './hotel-booking.component.html',
  styleUrls: ['./hotel-booking.component.css']
})
export class HotelBookingComponent implements OnInit, OnDestroy {
  
  hotelId!: number;
  hotel?: Hotel;
  bookingForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  averageRating: number = 0; // Store the average rating
  private destroy$ = new Subject<void>();
  showConfirmationPopup = false; // To toggle the confirmation popup
  bookingDetails: any = {}; // To store booking details for the popup
  userId!: number; // Store the userId
  minDate: string;
  minCheckoutDate: string;
  

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private hotelService: HotelService,
    private reviewService: ReviewService,
    private paymentService: PaymentService,
    private authService: AuthService, // Inject PaymentService
    private fb: FormBuilder
  ) {
    this.minDate = new Date().toISOString().split('T')[0];
    this.minCheckoutDate = this.minDate;
    this.initForm();
  }
  
  private initForm(): void {
    this.bookingForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required],
      guestsRooms: ['', [Validators.required, Validators.min(1)]],
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{10,12}$/)]],
      specialRequests: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe(params => {
      this.hotelId = +params['id'];
      this.loadHotelDetails();
      this.loadHotelRating(); 
    });
  
    // Retrieve userId from AuthService and handle null
    const userId = this.authService.getCurrentUserId();
    if (userId === null) {
      throw new Error('User ID is null. Please ensure the user is logged in.');
    }
    this.userId = userId; // Assign the valid userId
    console.log('Retrieved userId:', this.userId); // Debugging log
  }
  onCheckInChange(): void {
    const checkInDate = this.bookingForm.get('checkIn')?.value;
    if (checkInDate) {
      // Set minimum checkout date to the day after check-in
      const nextDay = new Date(checkInDate);
      nextDay.setDate(nextDay.getDate() + 1);
      this.minCheckoutDate = nextDay.toISOString().split('T')[0];
      
      // Reset checkout date if it's before new minimum
      const checkOutDate = this.bookingForm.get('checkOut')?.value;
      if (checkOutDate && checkOutDate < this.minCheckoutDate) {
        this.bookingForm.patchValue({ checkOut: '' });
      }
    }
  }
  onReviewSubmitted(): void {
    console.log('Review submitted. Refreshing average rating...');
    this.loadHotelRating(); // Refresh the average rating
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadHotelDetails(): void {
    this.isLoading = true;
    this.hotelService.getHotelById(this.hotelId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (hotel) => {
          this.hotel = hotel;
          this.isLoading = false;
        },
        error: (error) => {
          this.errorMessage = 'Error loading hotel details';
          this.isLoading = false;
          console.error('Error loading hotel:', error);
        }
      });
  }

  private loadHotelRating(): void {
    this.reviewService.getAverageRatingByHotelId(this.hotelId).pipe(takeUntil(this.destroy$)).subscribe({
      next: (averageRating) => {
        this.averageRating = averageRating; // Use the average rating from the service
      },
      error: (err) => {
        console.error(`Failed to load average rating for hotel ID ${this.hotelId}`, err);
        this.averageRating = 0; // Default to 0 if the average rating fails to load
      }
    });
  }

  onSubmit(): void {
    if (this.bookingForm.invalid || this.isLoading) {
      this.bookingForm.markAllAsTouched();
      return;
    }
  
    this.isLoading = true;
    const checkInDate = this.bookingForm.value.checkIn;
    const totalPrice = this.calculateTotalPrice();
    // Call availability API first
    this.hotelService.getAvailableHotels(this.hotel?.location || '', checkInDate)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          // Proceed with existing booking logic regardless of availability response
          this.bookingDetails = {
            hotelName: this.hotel?.name,
            email: this.bookingForm.value.email,
            checkIn: this.bookingForm.value.checkIn,
            checkOut: this.bookingForm.value.checkOut,
            guestsRooms: this.bookingForm.value.guestsRooms,
            phone: this.bookingForm.value.phone,
            specialRequests: this.bookingForm.value.specialRequests,
            totalPrice: this.calculateTotalPrice()
          };
  
          this.showConfirmationPopup = true;
          this.isLoading = false;
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = 'Failed to process your request. Please try again.';
          console.error('Error:', error);
        }
      });
  }


  confirmBooking(): void {
    const bookingData = {
      ...this.bookingForm.value,
      hotelId: this.hotelId,
      userId: this.userId, // Include userId
      totalPrice: this.calPrice()
    };
  
    console.log('Booking Data:', bookingData); 
  
    this.isLoading = true;
    this.clearMessages();
  
    // Format the booking date as YYYY-MM-DD
    const formattedBookingDate = new Date(bookingData.checkIn).toISOString().split('T')[0];
  
    // Call the PaymentService to book the hotel
    this.paymentService.bookHotel(
      bookingData.userId, // Pass userId
      bookingData.email,
      this.hotelId,
      formattedBookingDate,
      bookingData.guestsRooms // Pass quantity as guestsRooms
    ).subscribe({
      next: (response) => {
        console.log('Backend Response:', response); // Debugging log
        this.isLoading = false;
  
        // Store the session URL for the "Pay Now" button
        this.bookingDetails.sessionUrl = response.sessionUrl;
  
        // Show the "Pay Now" popup
        this.showConfirmationPopup = true;
  
        console.log('Session URL:', this.bookingDetails.sessionUrl); // Debugging log
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to initiate payment. Please try again.';
        console.error('Payment error:', err);
      }
    });
  }
  // confirmBooking(): void {
  //   const bookingData = {
  //     ...this.bookingForm.value,
  //     hotelId: this.hotelId,
  //     userId: this.userId, // Include userId
  //   };
  
  //   console.log('Booking Data:', bookingData); 
  
  //   this.isLoading = true;
  //   this.clearMessages();
  
  //   // Format the booking date as YYYY-MM-DD
  //   const formattedBookingDate = new Date(bookingData.checkIn).toISOString().split('T')[0];
  
  //   // Calculate the total price as hotel.pricePerNight * nights
  //   const checkIn = new Date(bookingData.checkIn);
  //   const checkOut = new Date(bookingData.checkOut);
  //   const nights = Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));
  //   const totalPrice = this.hotel?.pricePerNight ? this.hotel.pricePerNight * nights : 0;
  
  //   // Call the PaymentService to book the hotel
  //   this.paymentService.bookHotel(
  //     bookingData.userId, // Pass userId
  //     bookingData.email,
  //     this.hotelId,
  //     formattedBookingDate,
  //     bookingData.guestsRooms // Pass total price as the quantity
  //   ).subscribe({
  //     next: (response) => {
  //       console.log('Backend Response:', response); // Debugging log
  //       this.isLoading = false;
  
  //       // Store the session URL for the "Pay Now" button
  //       this.bookingDetails.sessionUrl = response.sessionUrl;
  
  //       // Show the "Pay Now" popup
  //       this.showConfirmationPopup = true;
  
  //       console.log('Session URL:', this.bookingDetails.sessionUrl); // Debugging log
  //     },
  //     error: (err) => {
  //       this.isLoading = false;
  //       this.errorMessage = 'Failed to initiate payment. Please try again.';
  //       console.error('Payment error:', err);
  //     }
  //   });
  // }
  calPrice(): number{
    const checkIn = new Date(this.bookingForm.value.checkIn);
    const checkOut = new Date(this.bookingForm.value.checkOut);
    const nights = Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));

    if (isNaN(nights) || nights <= 0) return 0;
    // const totalPrice = this.hotel?.pricePerNight ? this.hotel.pricePerNight * nights : 0;
    return this.hotel?.pricePerNight ? this.hotel.pricePerNight * nights : 0;
  
  }
  closePopup(): void {
    this.showConfirmationPopup = false; // Close the popup
  }

  private calculateTotalPrice(): number {
    if (!this.hotel?.pricePerNight) return 0;

    const checkIn = new Date(this.bookingForm.value.checkIn);
    const checkOut = new Date(this.bookingForm.value.checkOut);
    const nights = Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));

    if (isNaN(nights) || nights <= 0) return 0;

    const guestsRooms = Number(this.bookingForm.value.guestsRooms) || 1;
    return this.hotel.pricePerNight * nights * guestsRooms;
  }

  getErrorMessage(controlName: string): string {
    const control = this.bookingForm.get(controlName);
    if (!control?.errors || !control.touched) return '';

    const errors: { [key: string]: string } = {
      required: `${controlName} is required`,
      email: 'Invalid email address',
      min: `Minimum value is ${control.errors['min']?.min}`,
      minlength: `Minimum length is ${control.errors['minlength']?.requiredLength}`,
      pattern: 'Invalid format'
    };

    const errorKey = Object.keys(control.errors)[0];
    return errors[errorKey] || 'Invalid input';
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
 }