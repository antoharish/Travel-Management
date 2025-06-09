import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-package-add',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule], // Import ReactiveFormsModule for reactive forms
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddPackageComponent {
  packageForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    // Initialize the form with FormBuilder
    this.packageForm = this.fb.group({
      name: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]],
      noOfDays: [0, [Validators.required, Validators.min(1)]],
      noOfPeople: [0, [Validators.required, Validators.min(1)]],
      location: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      noOfPackages: [null, [Validators.required, Validators.min(1)]] // Ensure this field is included
    });
  }

  addPackage(): void {
    if (this.packageForm.valid) {
      console.log('Form Values:', this.packageForm.value); // Debugging log for form values

      const numberOfPackages = this.packageForm.get('noOfPackages')?.value; // Get value before reset

      this.http.post<{ packageId: number }>('http://localhost:9070/packages/createPackages', this.packageForm.value).subscribe({
        next: (response) => {
          alert('Package added successfully!');
          const packageId = response.packageId;
          console.log('Generated Package ID:', packageId); // Debugging log
          this.SetNoOfPackage(packageId, numberOfPackages); // Pass value here
          this.packageForm.reset(); // Reset after using the value
        },
        error: (error) => {
          console.error('Error adding package:', error);
          alert('Failed to add package.');
        }
      });
    } else {
      alert('Please fill out all required fields.');
    }
  }

  SetNoOfPackage(packageId: number, numberOfPackages: number): void {
    console.log('Setting number of packages:', numberOfPackages); // Debugging log

    if (numberOfPackages && numberOfPackages > 0) {
      this.http.post(`http://localhost:9070/packages/${packageId}/setPackages?numberOfPackages=${numberOfPackages}`, {}).subscribe({
        next: () => {
          alert('Number of packages set successfully!');
        },
        error: (error) => {
          console.error('Error setting number of packages:', error);
          alert('Failed to set number of packages.');
        }
      });
    } else {
      alert('Please fill out the number of packages field correctly.');
    }
  }
}