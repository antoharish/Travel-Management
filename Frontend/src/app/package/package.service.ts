import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; 
import {TravelPackage} from '../itinerery/models/itinerary.model'
import { HotelService } from '../services/hotel.service';

@Injectable({
  providedIn: 'root'
})
export class PackageService {
  errorMessage: string | undefined;
  isLoading: boolean | undefined;
  packages: any[] | undefined;
  formData: any;

  private baseUrl = 'http://localhost:9070';
  constructor(private http: HttpClient, private hotelservice: HotelService) {}

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

  getAllPackages(): Observable<TravelPackage[]> {
          return this.http.get<TravelPackage[]>(`${this.baseUrl}/packages`)
              .pipe(catchError(this.handleError));
      }

      private handleError(error: HttpErrorResponse): Observable<never> {
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