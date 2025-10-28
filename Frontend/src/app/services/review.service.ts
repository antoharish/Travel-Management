
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Review } from '../models/review.model';

@Injectable({
  providedIn: 'root'
})

export class ReviewService {
  private baseUrl: string = 'http://localhost:9070/api/review';

  constructor(private http: HttpClient) { }

  getReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(this.baseUrl).pipe(
      catchError(this.handleError)
    );
  }

  getReviewById(id: number): Observable<Review> {
    return this.http.get<Review>(`${this.baseUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getReviewsByHotelId(hotelId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.baseUrl}/hotel/${hotelId}`).pipe(
      catchError(this.handleError)
    );
  }

  getReviewsByUserId(userId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.baseUrl}/user/${userId}`).pipe(
      catchError(this.handleError)
    );
  }

  getReviewsWithRatingAbove(rating: number): Observable<Review[]> {
    if (rating < 1 || rating > 5) {
      return throwError(() => new Error('Rating must be between 1 and 5'));
    }
    return this.http.get<Review[]>(`${this.baseUrl}/rating/above/${rating}`).pipe(
      catchError(this.handleError)
    );
  }

  addReview(review: Review): Observable<Review> {
    if (!this.validateReview(review)) {
      return throwError(() => new Error('Invalid review data'));
    }
    return this.http.post<Review>(`${this.baseUrl}/user/postReview`, review).pipe(
      catchError(this.handleError)
    );
  }
  getAverageRatingByHotelId(hotelId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/hotel/${hotelId}/average-rating`).pipe(
      catchError(this.handleError)
    );
  }

  updateReview(id: number, review: Review): Observable<Review> {
    if (!this.validateReview(review)) {
      return throwError(() => new Error('Invalid review data'));
    }
    return this.http.put<Review>(`${this.baseUrl}/user/update/${id}`, review).pipe(
      catchError(this.handleError)
    );
  }

  deleteReview(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/user/delete/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private validateReview(review: Review): boolean {
    return !!(
      review &&
      review.hotel?.hotelId &&
      review.user?.userId &&
      review.rating >= 1 &&
      review.rating <= 5 &&
      review.comment?.trim()
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.error?.message || error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}