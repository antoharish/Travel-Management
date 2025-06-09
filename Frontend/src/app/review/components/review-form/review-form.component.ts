import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Review } from '../../../models/review.model';
import { ReviewService } from '../../../services/review.service';
import { StarRatingComponent } from '../../star-rating/star-rating.component';

@Component({
  selector: 'app-review-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, StarRatingComponent],
  templateUrl: './review-form.component.html',
  styleUrls: ['./review-form.component.css']
})
// export class ReviewFormComponent implements OnInit {
//   reviewForm!: FormGroup;
//   isEdit: boolean = false;
//   reviewId?: number;

//   constructor(
//     private fb: FormBuilder,
//     private reviewService: ReviewService,
//     private router: Router,
//     private route: ActivatedRoute
//   ) { }

//   ngOnInit(): void {
//     // Initialize the form with blank/default values
//     this.reviewForm = this.fb.group({
//       rating: [null, [Validators.required, Validators.min(0), Validators.max(5)]],
//       comment: ['', Validators.required],
//       hotelId: [null, Validators.required],
//       userId: [null, Validators.required]
//     });

    
//     this.route.paramMap.subscribe(params => {
//       const idParam = params.get('id');
//       if (idParam) {
//         this.isEdit = true;
//         this.reviewId = Number(idParam);
//         this.reviewService.getReviewById(this.reviewId).subscribe({
//           next: (review: Review) => {
//             this.reviewForm.patchValue({
//               rating: review.rating,
//               comment: review.comment,
//               hotelId: review.hotel.hotelId,
//               userId: review.user.userId
//             });
//           },
//           error: err => console.error(err)
//         });
//       }
//     });
//   }

//   onSubmit(): void {
//     if (this.reviewForm.invalid) {
//       return;
//     }

//     // Build the review object.
//     // (Here we build a minimal structure. In a real app, you might obtain more hotel/user details.)
//     const review: Review = {
//       hotel: { hotelId: this.reviewForm.value.hotelId },
//       user: { userId: this.reviewForm.value.userId },
//       rating: this.reviewForm.value.rating,
//       comment: this.reviewForm.value.comment
//     };

//     if (this.isEdit && this.reviewId) {
//       this.reviewService.updateReview(this.reviewId, review).subscribe({
//         next: () => this.router.navigate(['/reviews']),
//         error: err => console.error(err)
//       });
//     } else {
//       this.reviewService.addReview(review).subscribe({
//         next: () => this.router.navigate(['/reviews']),
//         error: err => console.error(err)
//       });
//     }
//   }
// }
export class ReviewFormComponent implements OnInit {
  reviewForm!: FormGroup;
  isEdit: boolean = false;
  reviewId?: number;
  stars: number[] = Array(5).fill(0); // Represents 5 stars

  constructor(
    private fb: FormBuilder,
    private reviewService: ReviewService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.reviewForm = this.fb.group({
      rating: [null, [Validators.required, Validators.min(1), Validators.max(5)]],
      comment: ['', Validators.required],
      hotelId: [null, Validators.required],
      userId: [null, Validators.required]
    });

    // Load existing review if editing
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.isEdit = true;
        this.reviewId = Number(idParam);
        this.reviewService.getReviewById(this.reviewId).subscribe({
          next: (review: Review) => {
            this.reviewForm.patchValue({
              rating: review.rating,
              comment: review.comment,
              hotelId: review.hotel.hotelId,
              userId: review.user.userId
            });
          },
          error: err => console.error(err)
        });
      }
    });
  }

  // Set the rating when a user clicks a star
  setRating(star: number): void {
    this.reviewForm.patchValue({ rating: star });
  }

  onSubmit(): void {
    if (this.reviewForm.invalid) {
      return;
    }

    const review: Review = {
      hotel: { hotelId: this.reviewForm.value.hotelId },
      user: { userId: this.reviewForm.value.userId },
      rating: this.reviewForm.value.rating,
      comment: this.reviewForm.value.comment
    };

    if (this.isEdit && this.reviewId) {
      this.reviewService.updateReview(this.reviewId, review).subscribe({
        next: () => this.router.navigate(['/reviews']),
        error: err => console.error(err)
      });
    } else {
      this.reviewService.addReview(review).subscribe({
        next: () => this.router.navigate(['/reviews']),
        error: err => console.error(err)
      });
    }
  }
}
