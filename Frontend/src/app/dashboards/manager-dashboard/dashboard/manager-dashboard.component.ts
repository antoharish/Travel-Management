import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AddHotelComponent } from '../add-hotel/add-hotel.component';
import { ManageHotelsComponent } from '../manage-hotels/manage-hotels.component';
import { NavBarComponent } from "../../../navigation/navigation.component";

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, AddHotelComponent, ManageHotelsComponent, NavBarComponent],
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.css']
})
export class ManagerDashboardComponent {
  selectedSection: 'addHotel' | 'manageHotels' | null = null;

  showSection(section: 'addHotel' | 'manageHotels'): void {
    this.selectedSection = section;
  }
}