
import { Component, Input, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ReviewService } from '../../../services/review.service';
import { AuthService } from '../../../services/auth.service';
import { Review } from '../../../models/review.model';
import { StarRatingComponent } from '../../star-rating/star-rating.component';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-review-by-hotel',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, StarRatingComponent],
  templateUrl: './review-by-hotel.component.html',
  styleUrls: ['./review-by-hotel.component.css']
})
export class ReviewByHotelComponent implements OnInit, OnDestroy {
  @Input() hotelId!: number;
  @Output() reviewSubmitted = new EventEmitter<void>();
  reviews: Review[] = [];
  filteredReviews: Review[] = [];
  reviewForm!: FormGroup;
  isSubmitting = false;
  errorMessage = '';
  successMessage = '';
  sortOrder: 'newest' | 'highest' | 'lowest' = 'newest';
  filterRating: number | null = null;
  isLoading = false;

  reviewStats = {
    averageRating: 0,
    totalReviews: 0,
    ratingDistribution: new Map<number, number>()
  };

  private destroy$ = new Subject<void>();
  userReview: Review | null = null;

  constructor(
    private reviewService: ReviewService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    if (!this.hotelId) {
      this.setError('Hotel ID is required');
      return;
    }
    this.initForm();
    this.loadReviews();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initForm(): void {
    this.reviewForm = this.fb.group({
      rating: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
      comment: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]]
    });
  }

  private loadReviews(): void {
    this.isLoading = true;
    this.reviewService.getReviewsByHotelId(this.hotelId).pipe(takeUntil(this.destroy$)).subscribe({
      next: (reviews) => {
        this.reviews = reviews;
        this.updateReviewStats();
        this.applyFiltersAndSort();
        this.findUserReview();
        this.isLoading = false;
      },
      error: (err) => {
        this.setError('Failed to load reviews');
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  private updateReviewStats(): void {
    if (!this.reviews.length) {
      this.resetStats();
      return;
    }

    const sum = this.reviews.reduce((acc, review) => acc + review.rating, 0);
    this.reviewStats.averageRating = sum / this.reviews.length;
    this.reviewStats.totalReviews = this.reviews.length;

    this.reviewStats.ratingDistribution.clear();
    this.reviews.forEach((review) => {
      const count = this.reviewStats.ratingDistribution.get(review.rating) || 0;
      this.reviewStats.ratingDistribution.set(review.rating, count + 1);
    });
  }

  private resetStats(): void {
    this.reviewStats = {
      averageRating: 0,
      totalReviews: 0,
      ratingDistribution: new Map()
    };
  }

  private findUserReview(): void {
    const userId = this.authService.getCurrentUserId();
    if (userId) {
      this.userReview = this.reviews.find((review) => review.user.userId === userId) || null;
      if (this.userReview) {
        this.reviewForm.patchValue({
          rating: this.userReview.rating,
          comment: this.userReview.comment
        });
      }
    }
  }

  getRatingPercentage(rating: number): number {
    if (!this.reviewStats.totalReviews) return 0;
    const count = this.reviewStats.ratingDistribution.get(rating) || 0;
    return (count / this.reviewStats.totalReviews) * 100;
  }

  filterByRating(rating: number | null): void {
    this.filterRating = rating;
    this.applyFiltersAndSort();
  }

  applyFiltersAndSort(): void {
    this.filteredReviews = [...this.reviews];

    if (this.filterRating) {
      this.filteredReviews = this.filteredReviews.filter((review) => review.rating === this.filterRating);
    }

    const sortFunctions = {
      newest: (a: Review, b: Review) => new Date(b.timestamp!).getTime() - new Date(a.timestamp!).getTime(),
      highest: (a: Review, b: Review) => b.rating - a.rating,
      lowest: (a: Review, b: Review) => a.rating - b.rating
    };

    this.filteredReviews.sort(sortFunctions[this.sortOrder]);
  }

  onRatingChange(rating: number): void {
    this.reviewForm.patchValue({ rating });
  }

  onSubmit(): void {
    if (this.reviewForm.invalid || this.isSubmitting) {
      this.reviewForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.clearMessages();

    const userId = this.authService.getCurrentUserId();
    if (!userId) {
      this.setError('Please log in to submit a review');
      this.isSubmitting = false;
      return;
    }

    const reviewData: Review = {
      ...this.reviewForm.value,
      hotel: { hotelId: this.hotelId },
      user: { userId },
      timestamp: new Date()
    };

    const reviewObservable = this.userReview
      ? this.reviewService.updateReview(this.userReview.reviewID!, reviewData)
      : this.reviewService.addReview(reviewData);

    reviewObservable.subscribe({
      next: (review) => {
        this.successMessage = this.userReview ? 'Review updated successfully!' : 'Review submitted successfully!';
        this.userReview = review;
        this.loadReviews();
        this.isSubmitting = false;
        this.reviewSubmitted.emit();
        
      },
      error: (err) => {
        this.setError('Failed to submit review');
        console.error(err);
        this.isSubmitting = false;
      }
      
    });
    
  }

  onDelete(): void {
    if (!this.userReview) return;

    this.isSubmitting = true;
    this.clearMessages();

    this.reviewService.deleteReview(this.userReview.reviewID!).subscribe({
      next: () => {
        this.successMessage = 'Review deleted successfully!';
        this.userReview = null;
        this.reviewForm.reset({ rating: 0, comment: '' });
        this.loadReviews();
        this.isSubmitting = false;
        this.reviewSubmitted.emit();
      },
      error: (err) => {
        this.setError('Failed to delete review');
        console.error(err);
        this.isSubmitting = false;
      }
    });
  }

  trackByReviewId(index: number, review: Review): number {
    return review.reviewID!;
  }

  getErrorMessage(controlName: string): string {
    const control = this.reviewForm.get(controlName);
    if (!control?.errors || !control.touched) return '';

    const errorMessages = {
      required: `${controlName} is required`,
      min: `${controlName} must be at least ${control.errors['min']?.min}`,
      max: `${controlName} must be at most ${control.errors['max']?.max}`,
      minlength: `${controlName} must be at least ${control.errors['minlength']?.requiredLength} characters`,
      maxlength: `${controlName} must be at most ${control.errors['maxlength']?.requiredLength} characters`
    };

    const firstError = Object.keys(control.errors)[0];
    return errorMessages[firstError as keyof typeof errorMessages] || 'Invalid input';
  }

  private setError(message: string): void {
    this.errorMessage = message;
    console.error(message);
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}