import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HotelService } from '../../../../../services/hotel.service';



@Component({
  selector: 'app-add-hotels-to-package',
  templateUrl: './add-hotels-to-package.component.html',
  imports: [
    CommonModule,
    FormsModule
  ],
  styleUrls: ['./add-hotels-to-package.component.css']
})
export class AddHotelsToPackageComponent implements OnInit {
  location: string = '';
  checkIn: string = '';
  checkOut: string = '';
  guestsRooms: string = '';
  availableHotels: any[] = [];
  selectedHotels: any[] = [];
  isLoading = false;
  errorMessage = '';
  isHotelsFormVisible = false; // State variable to track form visibility


  constructor(private hotelService: HotelService, private http: HttpClient) {}

  ngOnInit(): void {}

  toggleHotelsForm(): void {
    this.isHotelsFormVisible = !this.isHotelsFormVisible;
  }

  onSearch(): void {
    if (!this.location || !this.checkIn || !this.checkOut || !this.guestsRooms) {
      alert('Please fill in all the required fields.');
      return;
    }
  
    this.isLoading = true;
    this.errorMessage = '';
    this.hotelService.getAvailableHotels(this.location, this.checkIn).subscribe({
      next: (hotels) => {
        console.log('Hotels fetched:', hotels); // Log the response
        this.availableHotels = hotels; // Populate the availableHotels array
        this.isHotelsFormVisible = true; // Ensure the form is visible
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to fetch hotel availability. Please try again.';
        this.isLoading = false;
        console.error('Error fetching hotels:', err);
      }
    });
  }

  addToPackage(hotelWrapper: any): void {
    if (!hotelWrapper.packageId) {
      alert('Please enter a Package ID.');
      return;
    }
  
    const packageId = hotelWrapper.packageId;
    const hotelId = hotelWrapper.hotel.hotelId;
    const id = hotelWrapper.id; 
    // Assuming `id` is part of the hotelWrapper object
    console.log(id)
  
    const apiUrl = `http://localhost:9070/packages/${packageId}/hotels/${hotelId}/${id}`;
  
    this.http.post(apiUrl, {}).subscribe({
      next: () => {
        alert(`Hotel "${hotelWrapper.hotel.name}" added to package ID ${packageId} successfully!`);
        this.selectedHotels.push({ ...hotelWrapper.hotel, packageId }); // Add hotel with package ID to the selected list
      },
      error: (err: any) => {
        alert('Failed to add hotel to the package.');
        console.error('Error adding hotel:', err);
      }
    });
  }

  removeFromPackage(hotel: any): void {
    this.selectedHotels = this.selectedHotels.filter(h => h !== hotel);
  }

  savePackage(): void {
    console.log('Selected Hotels:', this.selectedHotels);
    alert('Hotels added to the package successfully!');
  }
}
