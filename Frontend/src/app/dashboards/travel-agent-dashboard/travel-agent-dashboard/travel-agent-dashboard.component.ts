import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AddHotelsToPackageComponent } from './packages/add-hotels-to-package/add-hotels-to-package.component';
import { AddActivityComponent } from './activities/add/add.component';
import { AddFlightToPackageComponent } from './packages/add-flight-to-package/add-flight-to-package.component';
import { AddActivityToPackageComponent } from './packages/add-activity-to-package/add-activity-to-package.component';
import { CommonModule } from '@angular/common';
import { NavBarComponent } from "../../../navigation/navigation.component";



@Component({
  selector: 'app-travel-agent-dashboard',
  standalone: true,
  imports: [RouterModule, CommonModule, NavBarComponent],
  templateUrl: './travel-agent-dashboard.component.html',
  styleUrls: ['./travel-agent-dashboard.component.css']
})
export class TravelAgentDashboardComponent {
  constructor(private router: Router) {}

  navigateTo(route: string): void {
    this.router.navigate([`/travel-agent-dashboard/${route}`]);
  }
}