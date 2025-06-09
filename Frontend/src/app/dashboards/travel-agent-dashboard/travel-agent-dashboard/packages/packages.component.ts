import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AddFlightToPackageComponent } from './add-flight-to-package/add-flight-to-package.component';
import { AddActivityToPackageComponent } from './add-activity-to-package/add-activity-to-package.component';
import { AddHotelsToPackageComponent } from './add-hotels-to-package/add-hotels-to-package.component';

@Component({
  selector: 'app-packages',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './packages.component.html',
  styleUrl: './packages.component.css'
})
export class PackagesComponent {
  constructor(private router: Router) {}
  navigateTo(route: string): void {
    this.router.navigate([`/travel-agent-dashboard/${route}`]);
  }
}
