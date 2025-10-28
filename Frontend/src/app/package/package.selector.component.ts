import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { PackageService } from './package.service';
import { TravelPackage } from '../itinerery/models/itinerary.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-package-selector',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './package.selector.component.html',
  styleUrls: ['./package.selector.component.css']
})
export class PackageSelectorComponent {
  formData = { startDate: '', endDate: '', people: 1, location: '' };
  packages: any[] = [];
  isLoading = false;
  errorMessage = '';
  allPackages: TravelPackage[] = [];
  today: string = " ";

  constructor(private http: HttpClient, private router: Router, private packageService: PackageService, private authservice: AuthService) {}

  ngOnInit(): void {
    // Load all packages by default when the component is initialized
    this.loadAllPackages();
    const currentDate = new Date();
    this.today = currentDate.toISOString().split('T')[0]; 
    // Enable drag functionality for the carousel

  }

  scrollLeft(): void {
    const track = document.getElementById('carouselTrack') as HTMLElement;
    if (track) {
      const scrollAmount = 300; // Adjust scroll amount
      track.scrollLeft -= scrollAmount;
      console.log('Scrolled left:', track.scrollLeft);
    } else {
      console.error('Carousel track element not found.');
    }
  }

  scrollRight(): void {
    const track = document.getElementById('carouselTrack') as HTMLElement;
    if (track) {
      const scrollAmount = 300; // Adjust scroll amount
      track.scrollLeft += scrollAmount;
      console.log('Scrolled right:', track.scrollLeft);
    } else {
      console.error('Carousel track element not found.');
    }
  }
  

  getMinEndDate(): string {
    if (this.formData.startDate) {
      const startDate = new Date(this.formData.startDate);
      startDate.setDate(startDate.getDate() + 1); // Add one day to the start date
      return startDate.toISOString().split('T')[0]; // Format as YYYY-MM-DD
    }
    return this.today; // Default to today's date if no start date is selected
  }

  private loadAllPackages(): void {
    this.isLoading = true;
    this.packageService.getAllPackages().subscribe({
      next: (packages) => {
        this.packages = packages; // Display all packages in cards
        this.isLoading = false;
      },
      error: (err) => {
        this.packages = [];
        this.errorMessage = 'Failed to load Packages';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  fetchPackages() {
    const params = new HttpParams()
      .set('startDate', this.formData.startDate)
      .set('endDate', this.formData.endDate)
      .set('location', this.formData.location)
      .set('noOfPeople', this.formData.people.toString());

    this.isLoading = true; // Start loading
    this.errorMessage = ''; // Clear previous error messages

    this.http.get<any[]>('http://localhost:9070/packages/searchPackage', { params }).subscribe(
      (response) => {
        if (response.length > 0 && response[0].packagesCreated === 0) {
          // No packages created
          this.packages = [];
        } else {
          // Packages are available
          this.packages = response;
        }
        this.isLoading = false; // Stop loading
      },
      (error) => {
        console.error('Error fetching packages', error);
        this.errorMessage = "Failed to fetch Packages. Please try again.";
        this.isLoading = false; // Stop loading
      }
    );
  }

  bookNow(packageId: number): void {

    if (!this.authservice.isLoggedIn()) {
      // Show a popup asking the user to log in
      alert('Please log in to proceed with booking.');
      return;
    }
    // Navigate to the itinerary page with the packageId as a route parameter
    console.log('Navigating to itinerary with packageId:', packageId);
    this.router.navigate(['/itinerary', packageId]);
  }
}
