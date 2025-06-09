import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HotelService } from '../../../services/hotel.service';
import { Hotel } from '../../../models/hotel.model';
import { EditHotelComponent } from '../edit-hotel/edit-hotel.component';
import { DeleteHotelComponent } from '../delete-hotel/delete-hotel/delete-hotel.component';
import { ReviewByHotelComponent } from '../../../review/components/review-by-hotel/review-by-hotel.component';
import { ReviewService } from '../../../services/review.service';
import { Router } from '@angular/router';
import { HotelAvailability } from '../../../models/hotelAvailability.model';

@Component({
  selector: 'app-manage-hotels',
  standalone: true,
  imports: [CommonModule, EditHotelComponent,DeleteHotelComponent],
  templateUrl: './manage-hotels.component.html',
  styleUrls: ['./manage-hotels.component.css']
})
export class ManageHotelsComponent implements OnInit {
  hotels: Hotel[] = [];
  selectedHotel: Hotel | null = null;
  showEditModal = false;
  showDeleteModal = false;
  allHotels: Hotel[] = [];


  constructor(
  private hotelService: HotelService,
    private reviewService: ReviewService, 
    private router: Router) {}

  ngOnInit(): void {
    this.loadHotels();
  }

  private loadHotels(): void {
    this.hotelService.getAllHotels().subscribe((hotels) => {
      this.hotels = hotels;
  
      // Fetch average rating for each hotel
      this.hotels.forEach((hotel) => {
        this.reviewService.getAverageRatingByHotelId(hotel.hotelId).subscribe({
          next: (averageRating) => {
            hotel.rating = averageRating; // Update the hotel's rating
          },
          error: (err) => {
            console.error(`Failed to fetch average rating for hotel ID ${hotel.hotelId}:`, err);
            hotel.rating = 0; // Default to 0 if there's an error
          }
        });
      });
    });
  }
  onEditClick(hotel: Hotel): void {
    this.selectedHotel = hotel;
    this.showEditModal = true;
  }

  onDeleteClick(hotel: Hotel): void {
    this.selectedHotel = hotel;
    this.showDeleteModal = true;
  }

  onHotelUpdated(): void {
    this.loadHotels();
    this.showEditModal = false;
    this.selectedHotel = null;
  }

  onHotelDeleted(): void {
    this.loadHotels();
    this.showDeleteModal = false;
    this.selectedHotel = null;
  }
}