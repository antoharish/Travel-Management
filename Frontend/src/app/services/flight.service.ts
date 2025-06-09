import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Flight } from '../models/flight.model';
import { FlightAvailability } from '../models/flightAvailability.model';
 
@Injectable({
    providedIn: 'root'
})
export class FlightService {
    private baseUrl = 'http://localhost:9070';
 
    constructor(private http: HttpClient) { }
 
    getAllFlights(): Observable<Flight[]> {
        return this.http.get<Flight[]>(`${this.baseUrl}/flights`)
            .pipe(catchError(this.handleError));
    }
 
    getFlightById(id: number): Observable<Flight> {
        return this.http.get<Flight>(`${this.baseUrl}/flights/${id}`)
            .pipe(catchError(this.handleError));
    }
 
    searchFlights(departureCity: string, destinationCity: string, date: string): Observable<FlightAvailability[]> {
        const params = new HttpParams()
            .set('source', departureCity)
            .set('destination', destinationCity)
            .set('date', date);
 
        return this.http.get<FlightAvailability[]>(`${this.baseUrl}/api/searchByDate`, { params })
            .pipe(catchError(this.handleError));
    }
 
    createFlight(flight: Flight): Observable<Flight> {
        return this.http.post<Flight>(`${this.baseUrl}/flights/create`, flight)
            .pipe(catchError(this.handleError));
    }
 
    updateFlight(id: number, flight: Flight): Observable<Flight> {
        return this.http.put<Flight>(`${this.baseUrl}/flights/${id}`, flight)
            .pipe(catchError(this.handleError));
    }
 
    deleteFlight(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/flights/${id}`)
            .pipe(catchError(this.handleError));
    }
 
    getFlightAvailability(id: number): Observable<FlightAvailability> {
        return this.http.get<FlightAvailability>(`${this.baseUrl}/api/flightAvailability/${id}`)
            .pipe(catchError(this.handleError));
    }
 
    createFlightAvailability(flightAvailability: FlightAvailability): Observable<FlightAvailability> {
        return this.http.post<FlightAvailability>(
            `${this.baseUrl}/api/flightAvailability/create`,
            flightAvailability
        ).pipe(catchError(this.handleError));
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
 