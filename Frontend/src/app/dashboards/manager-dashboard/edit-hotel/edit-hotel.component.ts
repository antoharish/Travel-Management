import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Hotel } from '../../../models/hotel.model';
import { HotelService } from '../../../services/hotel.service';

@Component({
  selector: 'app-edit-hotel',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './edit-hotel.component.html',
  styleUrls: ['./edit-hotel.component.css']
})
export class EditHotelComponent implements OnInit {
  @Input() hotel!: Hotel;
  @Output() hotelUpdated = new EventEmitter<void>();
  @Output() modalClosed = new EventEmitter<void>();

  editForm: FormGroup;
  isSubmitting = false;
  errorMessage = '';

  constructor(private fb: FormBuilder, private hotelService: HotelService) {
    this.editForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      location: ['', [Validators.required, Validators.minLength(3)]],
      roomsAvailable: ['', [Validators.required, Validators.min(0)]],
      pricePerNight: ['', [Validators.required, Validators.min(0)]],
      rating: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });
  }

  ngOnInit() {
    if (this.hotel) {
      this.editForm.patchValue({
        name: this.hotel.name,
        location: this.hotel.location,
        roomsAvailable: this.hotel.roomsAvailable,
        pricePerNight: this.hotel.pricePerNight,
        rating: this.hotel.rating
      });
    }
  }

  onSubmit() {
    if (this.editForm.valid && this.hotel?.hotelId && !this.isSubmitting) {
      this.isSubmitting = true;
      this.errorMessage = '';

      const updatedHotel: Hotel = {
        ...this.hotel,
        ...this.editForm.value
      };

      this.hotelService.updateHotel(this.hotel.hotelId, updatedHotel)
        .subscribe({
          next: () => {
            this.hotelUpdated.emit();
            this.modalClosed.emit();
          },
          error: (error) => {
            this.errorMessage = error.error?.message || 'Error updating hotel. Please try again.';
            this.isSubmitting = false;
          },
          complete: () => {
            this.isSubmitting = false;
          }
        });
    } else {
      this.markFormGroupTouched(this.editForm);
    }

}
  onClose() {
    if (!this.isSubmitting) {
      this.modalClosed.emit();
    }
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  getErrorMessage(controlName: string): string {
    const control = this.editForm.get(controlName);
    if (control?.errors) {
      if (control.errors['required']) return `${controlName} is required`;
      if (control.errors['minlength']) return `${controlName} must be at least ${control.errors['minlength'].requiredLength} characters`;
      if (control.errors['min']) return `${controlName} must be at least ${control.errors['min'].min}`;
      if (control.errors['max']) return `${controlName} must be at most ${control.errors['max'].max}`;
    }
    return '';
  }
}