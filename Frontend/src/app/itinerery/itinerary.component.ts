import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ActivitySelectorComponent } from '../activity-selector/activity-selector.component';
import { PaymentCardComponent } from '../payment-card/payment-card.component';
import { AuthService } from '../services/auth.service'; // Import AuthService
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel
import { NavBarComponent } from '../navigation/navigation.component'; // Import NavBarComponent


@Component({
  selector: 'app-itinerary',
  templateUrl: './itinerary.component.html',
  styleUrls: ['./itinerary.component.css'],
  imports: [CommonModule, FormsModule, NavBarComponent]
})
export class ItineraryComponent implements OnInit {

  packageId: number = 0;
  itinerary: any = null;
  days: any[] = [];
  totalPrice: number = 0; // Total price of the itinerary
  customizedActivities: any[] = []; 
  isActivitySelectorOpen: boolean = false;
  availableActivities: any[] = [];
  selectedDay: number | null = null;
// List of customized activities

  @ViewChild(ActivitySelectorComponent) activitySelector!: ActivitySelectorComponent;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private authService: AuthService, // Inject AuthService
    private router: Router // Inject Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.packageId = +params['packageId'];
      this.loadItinerary();
    });
  }

  // Load the itinerary for the selected package
  loadItinerary(): void {
    const userId = this.authService.getCurrentUserId(); // Get the current user ID
    if (!userId) {
      console.error('User is not logged in or token is invalid.');
      return;
    }
     
    const requestBody = { userId: userId, packageId: this.packageId };
    this.http.post<any>('http://localhost:9070/itineraries', requestBody).subscribe(
      (response) => {
        if(response){
        this.itinerary = response;
        this.totalPrice = response.totalPrice; // Initialize total price
        this.generateDays();
        this.updateCustomizedActivities(); // Initialize customized activities
      } else{
        console.error('Itinerary response is empty.');
        alert('Failed to load itinerary. Please try again.');
      }
    },
    (error) => {
      console.error('Error loading itinerary:', error);
      alert('An error occurred while loading the itinerary. Please try again.');
    }
  
    );
  }

  // Generate days for the itinerary based on the package duration
  generateDays(): void {
    const duration = this.itinerary?.travelPackage?.noOfDays || 0;
    const includedActivities = this.itinerary?.travelPackage?.activities || [];

    this.days = Array.from({ length: duration }, (_, i) => ({
      day: i + 1,
      hotel: i === 0 ? this.itinerary?.travelPackage?.includedHotels[0] || null : null,
      activities: []
    }));

    includedActivities.forEach((activity: any, index: number) => {
      const dayIndex = index % duration;
      this.days[dayIndex].activities.push(activity);
    });
  }

  // Open the activity selector for the selected day
  openActivitySelector(day: number): void {
    this.selectedDay = day;
    this.isActivitySelectorOpen = true;

    const location = this.itinerary?.travelPackage?.location;
    if (!location) {  
      console.error('Location is not available in the itinerary.');
      return;
    }

    console.log(`Fetching activities for location: ${location}`);
  
    // Fetch available activities for the location
    this.http.get<any[]>(`http://localhost:9070/activities/GetActivityByLocation?location=${location}`).subscribe({
      next: (response) => {
        this.availableActivities = response;
      },
      error: (error) => {
        console.error('Error fetching activities:', error);
      }
    });
  }

  closeActivitySelector(): void {
    this.isActivitySelectorOpen = false;
    this.selectedDay = null;
  }

  

  // Add an activity to the itinerary
  addActivity(activity: any): void {
    if (this.selectedDay === undefined) return;

    const day = this.days.find(d => d.day === this.selectedDay);
    if (day) {
      day.activities.push(activity);
    }

    this.http.post<any>(`http://localhost:9070/itineraries/${this.itinerary.itinerary_id}/addActivity?activityId=${activity.activityId}`, {}).subscribe(
      (response) => {
        this.itinerary = response;
        this.totalPrice = response.totalPrice; // Update total price
        this.updateCustomizedActivities(); // Update customized activities
        console.log(`Activity added to Day ${this.selectedDay}:`, activity);
      },
      (error) => {
        console.error('Error adding activity:', error);
      }
    );
  }

  // Remove an activity from the itinerary
  removeActivity(activityId: number, dayNumber: number): void {
    const day = this.days.find(d => d.day === dayNumber);
    if (day) {
      day.activities = day.activities.filter((activity: { activityId: number }) => activity.activityId !== activityId);
    }

    this.http.delete<any>(`http://localhost:9070/itineraries/${this.itinerary.itinerary_id}/removeActivity?activityId=${activityId}`).subscribe(
      (response) => {
        this.itinerary = response;
        this.totalPrice = response.totalPrice; // Update total price
        this.updateCustomizedActivities(); // Update customized activities
        console.log(`Activity removed from Day ${dayNumber}:`, activityId);
      },
      (error) => {
        console.error('Error removing activity:', error);
      }
    );
  }

  // Update the list of customized activities
  updateCustomizedActivities(): void {
    this.customizedActivities = this.days.flatMap(day => day.activities);
  }

  redirectToLogin() {
    window.location.href = '/login'; // Redirect to login if user is not authenticated
    }

  // Proceed to payment
  proceedToPayment(): void {
    console.log('Proceeding to payment...');
    const itineraryId = this.itinerary?.itinerary_id;
    console.log(itineraryId)

    if (!itineraryId) {
      console.error('Itinerary ID is missing.');
      return;
    }

    const userId = this.authService.getCurrentUserId(); // Get the current user ID dynamically
    const email = this.authService.getCurrentUserEmail(); // Get the current user email dynamically

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
    const quantity = this.itinerary?.quantity || 1; // Replace with the actual quantity if applicable

    // Construct the URL with query parameters
    const url = `http://localhost:9070/api/payments/package?userId=${userId}&email=${
      email
    }&itineraryId=${itineraryId}&bookingDate=${bookingDate}&quantity=${quantity}`;

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

  // Proceed to booking
  proceedToBooking(): void {

    if (!this.itinerary?.itinerary_id) {
      console.error('Itinerary ID is missing.');
      return;
    }
    this.router.navigate(['/itinerary-booking'], {
      queryParams: {
        itineraryId: this.itinerary?.itinerary_id,
        hotelPrice: this.calculateHotelPrice(),
        flightPrice: this.calculateFlightPrice(),
        activitiesPrice: this.calculateActivitiesPrice(),
      totalPrice: this.totalPrice
      }
    });
  }

  calculateHotelPrice(): number {
    return this.days.reduce((total, day) => total + (day.hotel?.pricePerNight || 0), 0);
  }

  calculateFlightPrice(): number {
    return this.itinerary?.travelPackage?.includedFlights?.[0]?.price || 0;
  }

  calculateActivitiesPrice(): number {
    return this.customizedActivities.reduce((total, activity) => total + activity.price, 0);
  }

  calculateTotalPrice(): number {
    return this.calculateHotelPrice() + this.calculateFlightPrice() + this.calculateActivitiesPrice();
  }
}
