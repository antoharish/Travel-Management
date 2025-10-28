import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Itinerary } from '../models/itinerary.model';

@Injectable({
  providedIn: 'root'
})
export class ItineraryService {
  private baseUrl = 'http://localhost:9070/itineraries';

  constructor(private http: HttpClient) {}

  // Load itinerary by package ID
  loadItinerary(userId: number, packageId: number): Observable<Itinerary> {
    const requestBody = { userId, packageId };
    return this.http.post<Itinerary>(`${this.baseUrl}`, requestBody);
  }

  // Add an activity to the itinerary
  addActivity(itineraryId: number, activityId: number): Observable<Itinerary> {
    return this.http.post<Itinerary>(`${this.baseUrl}/${itineraryId}/addActivity?activityId=${activityId}`, {});
  }

  // Remove an activity from the itinerary
  removeActivity(itineraryId: number, activityId: number): Observable<Itinerary> {
    return this.http.delete<Itinerary>(`${this.baseUrl}/${itineraryId}/removeActivity?activityId=${activityId}`);
  }
}