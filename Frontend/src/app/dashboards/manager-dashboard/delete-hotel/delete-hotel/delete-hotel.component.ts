import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Hotel } from '../../../../models/hotel.model';
import { HotelService } from '../../../../services/hotel.service';

@Component({
  selector: 'app-delete-hotel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './delete-hotel.component.html',
  styleUrls: ['./delete-hotel.component.css']
})
export class DeleteHotelComponent {
  @Input() hotel!: Hotel;
  @Output() hotelDeleted = new EventEmitter<void>();
  @Output() modalClosed = new EventEmitter<void>();
  
  isDeleting = false;
  errorMessage = '';

  constructor(private hotelService: HotelService) {}

  onConfirmDelete(): void {
    if (this.hotel?.hotelId && !this.isDeleting) {
      this.isDeleting = true;
      this.errorMessage = '';

      this.hotelService.deleteHotel(this.hotel.hotelId).subscribe({
        next: () => {
          this.hotelDeleted.emit();
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Error deleting hotel. Please try again.';
          this.isDeleting = false;
        },
        complete: () => {
          this.isDeleting = false;
        }
      });
    }
  }

  onClose(): void {
    if (!this.isDeleting) {
      this.modalClosed.emit();
    }
  }
}