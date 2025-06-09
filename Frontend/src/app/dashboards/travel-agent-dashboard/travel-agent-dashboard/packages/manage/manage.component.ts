import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-package-manage',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], // Import ReactiveFormsModule for the edit form
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css']
})
export class ManagePackageComponent implements OnInit {
  packages: any[] = []; // Array to store the list of packages
  editForm: FormGroup | null = null; // Form for editing a package
  editingPackageId: number | null = null; // ID of the package being edited
  deletingPackageId: number | null = null; // ID of the package being deleted

  constructor(private http: HttpClient, private fb: FormBuilder) {}

  ngOnInit() {
    this.fetchPackages();
  }

  fetchPackages() {
    this.http.get<any[]>('http://localhost:9070/packages').subscribe({
      next: (data) => {
        this.packages = data;
      },
      error: (error) => {
        console.error('Error fetching packages:', error);
      }
    });
  }

  editPackage(packageData: any): void {
    this.editingPackageId = packageData.packageId; // Set the ID of the package being edited
    this.editForm = this.fb.group({
      name: [packageData.name, Validators.required],
      price: [packageData.price, [Validators.required, Validators.min(1)]],
      noOfDays: [packageData.noOfDays, Validators.required],
      noOfPeople: [packageData.noOfPeople, Validators.required],
      location: [packageData.location, Validators.required],
      startDate: [packageData.startDate, Validators.required],
      endDate: [packageData.endDate, Validators.required],
      packagesCreated: [packageData.packagesCreated, Validators.required],
      packagetotalPrice: [packageData.packagetotalPrice, Validators.required]
    });
  }

  updatePackage(): void {
  if (this.editForm?.valid && this.editingPackageId !== null) {
    this.http.put(`http://localhost:9070/packages/${this.editingPackageId}`, this.editForm.value).subscribe({
      next: () => {
        alert('Package updated successfully!');
        this.editingPackageId = null; // Clear the editing state
        this.editForm = null; // Clear the form
        this.fetchPackages(); // Refresh the list
      },
      error: (error) => {
        console.error('Error updating package:', error);
        alert('Failed to update package.');
      }
    });
  }
}

  cancelEdit() {
    this.editingPackageId = null; // Clear the editing state
    this.editForm = null; // Clear the form
  }

  deletePackage(packageData: any): void {
    console.log('Package data received for deletion:', packageData); // Debugging log
  
    // Check for the correct property (packageId)
    if (!packageData || !packageData.packageId) {
      console.error('Invalid package data:', packageData);
      alert('Failed to delete package. Invalid package data.');
      return;
    }
  
    console.log('Deleting package with ID:', packageData.packageId); // Debugging statement
    this.deletingPackageId = packageData.packageId; // Set the ID of the package being deleted
    console.log(this.deletingPackageId); // Log the ID to ensure it's set correctly
  
    // Call the backend to delete the package
    this.http.delete(`http://localhost:9070/packages/${this.deletingPackageId}`).subscribe({
      next: () => {
        alert('Package deleted successfully!');
        this.fetchPackages(); // Refresh the list after deletion
      },
      error: (error) => {
        console.error('Error deleting package:', error);
        alert('Failed to delete package.');
      }
    });
  }
}