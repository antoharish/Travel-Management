import { Component, OnInit } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { CommonModule } from '@angular/common';
 
@Component({

  selector: 'app-activity-manage',

  templateUrl: './manage.component.html',

  styleUrls: ['./manage.component.css'],

  standalone: true,

  imports: [ReactiveFormsModule,CommonModule]

})

export class ManageActivityComponent implements OnInit {

  activities: any[] = [];

  editForm!: FormGroup;

  editingActivityId: number | null = null;
 
  constructor(private http: HttpClient, private fb: FormBuilder) {}
 
  ngOnInit() {

    this.fetchActivities();

  }
 
  fetchActivities() {

    this.http.get<any[]>('http://localhost:9070/activities').subscribe({

      next: (data) => this.activities = data,

      error: (error) => {

        console.error('Error fetching activities:', error);

        alert('Failed to fetch activities.');

      }

    });

  }
 
  editActivity(activity: any) {

    this.editingActivityId = activity.activityId;

    this.editForm = this.fb.group({

      name: [activity.name, Validators.required],

      location: [activity.location, Validators.required],

      description: [activity.description, Validators.required],

      price: [activity.price, [Validators.required, Validators.min(1)]]

    });

  }
 
  saveActivity() {

    if (this.editForm.valid && this.editingActivityId !== null) {

      this.http.put<any>(`http://localhost:9070/activities/${this.editingActivityId}`, this.editForm.value).subscribe({

        next: (updatedActivity) => {

          alert('Activity updated successfully!');

          this.editingActivityId = null;      // <-- This hides the form

          this.fetchActivities();

        },

        error: (error) => {

          console.error('Error updating activity:', error);

          alert('Failed to update activity.');

        }

      });

    }

  }
 
  cancelEdit() {

    this.editingActivityId = null;

    if (this.editForm) this.editForm.reset();

  }
 
  deleteActivity(activity: any) {

    if (!activity || !activity.activityId) {

      alert('Failed to delete activity. Invalid activity data.');

      return;

    }

    this.http.delete(`http://localhost:9070/activities/${activity.activityId}`).subscribe({

      next: () => {

        alert('Activity deleted successfully!');

        this.fetchActivities();

      },

      error: (error) => {

        console.error('Error deleting activity:', error);

        alert('Failed to delete activity.');

      }

    });

  }

}
 