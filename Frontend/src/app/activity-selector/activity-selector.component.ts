import { Component, EventEmitter, Input, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-activity-selector',
  templateUrl: './activity-selector.component.html',
  styleUrls: ['./activity-selector.component.css'],
  standalone: true,
  imports: [CommonModule] // Add necessary imports if needed
})
export class ActivitySelectorComponent {
  @Input() location: string = ''; // Location passed from the itinerary
  @Output() activityAdded = new EventEmitter<any>(); // Event emitter to notify parent component
  activities: any[] = []; // List of activities
  isVisible: boolean = false; // Controls the visibility of the sliding window

  constructor(private http: HttpClient) {}

  // Fetch activities based on location
  fetchActivities(): void {
    this.http.get<any>(`http://localhost:9070/activities/GetActivityByLocation?location=${this.location}`).subscribe(
      (response) => {
        // Ensure the response is an array
        if (Array.isArray(response)) {
          this.activities = response;
        } else if (response) {
          this.activities = [response]; // Wrap single object in an array
        } else {
          this.activities = []; // Handle empty response
        }
      },
      (error) => {
        console.error('Error fetching activities:', error);
        this.activities = []; // Reset activities on error
      }
    );
  }

  // Show the activity selector
  open(): void {
    this.isVisible = true;
    this.fetchActivities();
  }

  // Hide the activity selector
  close(): void {
    this.isVisible = false;
  }

  // Add an activity to the itinerary
  addActivity(activity: any): void {
    this.activityAdded.emit(activity); // Emit the selected activity to the parent component
    this.close(); // Close the activity selector
  }
}
