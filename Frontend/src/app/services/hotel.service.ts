// import { Injectable } from '@angular/core';
// import { HttpClient, HttpErrorResponse } from '@angular/common/http';
// import { Observable, throwError } from 'rxjs';
// import { catchError } from 'rxjs/operators';
// import { Hotel } from '../models/hotel.model';
// import { HotelAvailability } from '../models/hotelAvailability.model';

// @Injectable({
//     providedIn: 'root'
// })
// export class HotelService {
//     private baseUrl = 'http://localhost:9070';

//     constructor(private http: HttpClient) {}

//     getAllHotels(): Observable<Hotel[]> {
//         return this.http.get<Hotel[]>(`${this.baseUrl}/hotels`)
//             .pipe(catchError(this.handleError));
//     }

//     getHotelById(id: number): Observable<Hotel> {
//         return this.http.get<Hotel>(`${this.baseUrl}/hotels/${id}`)
//             .pipe(catchError(this.handleError));
//     }

//     getAvailableHotels(location: string, date: string): Observable<HotelAvailability[]> {
//         return this.http.get<HotelAvailability[]>
//         (`${this.baseUrl}/hotel-availability?location=${location}&date=${date}`)
//             .pipe(catchError(this.handleError));
//     }

//     createHotel(hotel: Hotel): Observable<Hotel> {
//         return this.http.post<Hotel>(`${this.baseUrl}/hotels/create`, hotel)
//             .pipe(catchError(this.handleError));
//     }

//     updateHotel(id: number, hotel: Hotel): Observable<Hotel> {
//         return this.http.put<Hotel>(`${this.baseUrl}/hotels/${id}`, hotel)
//             .pipe(catchError(this.handleError));
//     }

//     deleteHotel(id: number): Observable<void> {
//         return this.http.delete<void>(`${this.baseUrl}/hotels/${id}`)
//             .pipe(catchError(this.handleError));
//     }
//     createHotelAvailability(hotelAvailability: HotelAvailability): Observable<HotelAvailability> {
//         return this.http.post<HotelAvailability>(`${this.baseUrl}/hotel-availability/create`, hotelAvailability)
//             .pipe(catchError(this.handleError));
//     }

//     private handleError(error: HttpErrorResponse): Observable<never> {
//         let errorMessage = 'An error occurred';
//         if (error.error instanceof ErrorEvent) {
//             // Client-side error
//             errorMessage = `Error: ${error.error.message}`;
//         } else {
//             // Server-side error
//             errorMessage = `Error Code: ${error.status}\nMessage: ${error.error?.message || error.message}`;
//         }
//         console.error(errorMessage);
//         return throwError(() => new Error(errorMessage));
//     }
// }
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Hotel } from '../models/hotel.model';
import { HotelAvailability } from '../models/hotelAvailability.model';

@Injectable({
    providedIn: 'root'
})
export class HotelService {
    private baseUrl = 'http://localhost:9070';

    constructor(private http: HttpClient) {}

    getAllHotels(): Observable<Hotel[]> {
        return this.http.get<Hotel[]>(`${this.baseUrl}/hotels`)
            .pipe(catchError(this.handleError));
    }

    getHotelById(id: number): Observable<Hotel> {
        return this.http.get<Hotel>(`${this.baseUrl}/hotels/${id}`)
            .pipe(catchError(this.handleError));
    }

    getAvailableHotels(location: string, date: string): Observable<HotelAvailability[]> {
        return this.http.get<HotelAvailability[]>
        (`${this.baseUrl}/hotel-availability?location=${location}&date=${date}`)
            .pipe(catchError(this.handleError));
    }

    getHotelAvailability(hotelId: number, date: string): Observable<HotelAvailability> {
        const params = new HttpParams()
            .set('hotelId', hotelId.toString())
            .set('date', date);
    
        return this.http.get<HotelAvailability>(`${this.baseUrl}/hotel-availability/single`, { params })
            .pipe(catchError(this.handleError));
    }
    createHotel(hotel: Hotel): Observable<Hotel> {
        return this.http.post<Hotel>(`${this.baseUrl}/hotels/create`, hotel)
            .pipe(catchError(this.handleError));
    }

    updateHotel(id: number, hotel: Hotel): Observable<Hotel> {
        return this.http.put<Hotel>(`${this.baseUrl}/hotels/${id}`, hotel)
            .pipe(catchError(this.handleError));
    }

    deleteHotel(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/hotels/${id}`)
            .pipe(catchError(this.handleError));
    }
    createHotelAvailability(hotelAvailability: HotelAvailability): Observable<HotelAvailability> {
        return this.http.post<HotelAvailability>(`${this.baseUrl}/hotel-availability/create`, hotelAvailability)
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