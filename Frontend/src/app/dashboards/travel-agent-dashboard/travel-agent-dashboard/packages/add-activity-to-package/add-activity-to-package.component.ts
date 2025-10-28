import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';

@Component({
  selector: 'app-add-activity-to-package',
  standalone: true,
  templateUrl: './add-activity-to-package.component.html',
  imports: [
    CommonModule,
    FormsModule
    
  ],
  styleUrls: ['./add-activity-to-package.component.css']
})
export class AddActivityToPackageComponent implements OnInit {
  location: string = '';
  date: string = '';
  availableActivities: any[] = [];
  selectedActivities: any[] = [];
  isLoading = false;
  errorMessage = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {}

  // Fetch activities by location
  onSearch(): void {
    if (!this.location) {
      alert('Please enter a location.');
      return;
    }

    const apiUrl = `http://localhost:9070/activities/GetActivityByLocation?location=${this.location}`;
    this.isLoading = true;
    this.errorMessage = '';
    this.http.get<any[]>(apiUrl).subscribe({
      next: (activities) => {
        this.availableActivities = activities; // Store the activities from the response
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to fetch activity availability. Please try again.';
        this.isLoading = false;
        console.error('Error fetching activities:', err);
      }
    });
  }

  // Add activity to the package
  addToPackage(activity: any): void {
    if (!activity.packageId) {
      alert('Please enter a Package ID.');
      return;
    }

    const apiUrl = `http://localhost:9070/packages/${activity.packageId}/activities/${activity.activityId}`;
    this.http.post(apiUrl, {}).subscribe({
      next: () => {
        alert(`Activity "${activity.name}" added to package ID ${activity.packageId} successfully!`);
        this.selectedActivities.push(activity); // Add activity to the selected list
      },
      error: (err) => {
        alert('Failed to add activity to the package.');
        console.error('Error adding activity:', err);
      }
    });
  }

  // Remove activity from the selected list
  removeFromPackage(activity: any): void {
    this.selectedActivities = this.selectedActivities.filter(a => a !== activity);
  }

  // Save the selected activities to the package
  savePackage(): void {
    console.log('Selected Activities:', this.selectedActivities);
    alert('Activities added to the package successfully!');
  }
}
