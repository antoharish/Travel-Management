import { Routes } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { ReviewListComponent } from './review/components/review-list/review-list.component';
import { ReviewFormComponent } from './review/components/review-form/review-form.component';
import { ReviewDetailComponent } from './review/components/review-detail/review-detail.component';
import { HotelsComponent } from './hotels/hotels.component';
import { LoginComponent } from './auth/auth/login/login.component';
import { SignupComponent } from './auth/auth/signup/signup.component';
import { AdminDashboardComponent } from './dashboards/admin-dashboard/admin-dashboard.component';
import { AgentDashboardComponent } from './dashboards/agent-dashboard/agent-dashboard.component';
import { ManagerDashboardComponent } from './dashboards/manager-dashboard/dashboard/manager-dashboard.component';
import { ReviewByHotelComponent } from './review/components/review-by-hotel/review-by-hotel.component';
import { HotelBookingComponent } from './hotels/hotel-booking/hotel-booking.component';
import { PackageSelectorComponent } from './package/package.selector.component';
import { ItineraryComponent } from './itinerery/itinerary.component';

import { FlightBookingComponent } from './flights/flight-booking/flight-booking.component';
import { AuthGuard } from './guards/auth.guard';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { ProfileComponent } from './profile/profile.component';
import { FlightsComponent } from './flights/flights.component';
import { TravelAgentDashboardComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/travel-agent-dashboard.component';
import { AddHotelsToPackageComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/packages/add-hotels-to-package/add-hotels-to-package.component';
import { AddFlightToPackageComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/packages/add-flight-to-package/add-flight-to-package.component';
import { AddActivityToPackageComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/packages/add-activity-to-package/add-activity-to-package.component';
import { PackagesComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/packages/packages.component';
import { AddPackageComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/packages/add/add.component';
import { ManagePackageComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/packages/manage/manage.component';
import { ActivitiesComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/activities/activities.component';
import { AddActivityComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/activities/add/add.component';
import { ManageActivityComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/activities/manage/manage.component';
import { FlightsdashComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/flights/flights.component';
import { AddFlightComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/flights/add/add.component';
import { ManageFlightComponent } from './dashboards/travel-agent-dashboard/travel-agent-dashboard/flights/manage/manage.component';
import { ItineraryBookingComponent } from './itinerary-booking/itinerary-booking.component';

export const routes: Routes = [
  // When the URL is empty, redirect to Home
  { path: '', redirectTo: 'Home', pathMatch: 'full' },
 
  // Home route
  { path: 'Home', component: HomePageComponent },
 
  { path: 'flights', component: FlightsComponent },
  { path: 'flight-booking/:id', component: FlightBookingComponent },
  // Review routes
  { path: 'reviews', component: ReviewListComponent },
  { path: 'reviews/new', component: ReviewFormComponent },
  { path: 'reviews/:id', component: ReviewDetailComponent },
  { path: 'reviews/edit/:id', component: ReviewFormComponent },
 
  // Hotels route
  { path: 'hotels', component: HotelsComponent},
  { path: 'itinerary/:packageId', component: ItineraryComponent },
  {path:'packages',component:PackageSelectorComponent},
 
    //auth routes
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    //dashboards routes
    {
      path: 'admin-dashboard',
      component: AdminDashboardComponent,
      canActivate: [AuthGuard],
      data: { roles: ['ADMIN'] }
    },
    {
      path: 'manager-dashboard',
      component: ManagerDashboardComponent,
      canActivate: [AuthGuard],
      data: { roles: ['HOTELMANAGER'] }
    },
    {
      path: 'hotels/:id/reviews',
      component: ReviewByHotelComponent
    },
    { path: 'hotel-booking/:id', component: HotelBookingComponent },
    //all travel-agent-routes
   
    {
      path: 'travel-agent-dashboard',
      component: TravelAgentDashboardComponent,
      canActivate: [AuthGuard],
      data: { roles: ['TRAVELAGENT'] },
      children: [
        {
          path: 'app-add-hotels-to-package',
          component: AddHotelsToPackageComponent,
          canActivate: [AuthGuard],
          data: { roles: ['TRAVELAGENT'] }
        },
        {
          path: 'app-add-flight-to-package',
          component: AddFlightToPackageComponent,
          canActivate: [AuthGuard],
          data: { roles: ['TRAVELAGENT'] }
        },
        {
          path: 'app-add-activity-to-package',
          component: AddActivityToPackageComponent,
          canActivate: [AuthGuard],
          data: { roles: ['TRAVELAGENT'] }
        },
       
      {
          path: 'packages',
          component: PackagesComponent,
          canActivate: [AuthGuard],
          data: { roles: ['TRAVELAGENT'] },
          children: [
            {
              path: 'add',
              component: AddPackageComponent,
              canActivate: [AuthGuard],
              data: { roles: ['TRAVELAGENT'] }
            },
            {
              path: 'manage',
              component: ManagePackageComponent,
              canActivate: [AuthGuard],
              data: { roles: ['TRAVELAGENT'] }
            }
           
           
 
          ]
        },
        {
          path: 'activities',
          component: ActivitiesComponent,
          canActivate: [AuthGuard],
          data: { roles: ['TRAVELAGENT'] },
          children: [
            {
              path: 'add',
              component: AddActivityComponent,
              canActivate: [AuthGuard],
              data: { roles: ['TRAVELAGENT'] }
            },
            {
              path: 'manage',
              component: ManageActivityComponent,
              canActivate: [AuthGuard],
              data: { roles: ['TRAVELAGENT'] }
            }
          ]
        },
        {
          path: 'flightsComponent',
          component: FlightsdashComponent,
          canActivate: [AuthGuard],
          data: { roles: ['TRAVELAGENT'] },
          children: [
            {
              path: 'add',
              component: AddFlightComponent,
              canActivate: [AuthGuard],
              data: { roles: ['TRAVELAGENT'] }
            },
            {
              path: 'manage',
              component: ManageFlightComponent,
              canActivate: [AuthGuard],
              data: { roles: ['TRAVELAGENT'] }
            }
          ]
        }
      ]
    },
    //Payment success
    { path: 'payment-success', loadComponent: () => import('./payment-success/payment-success.component').then(m => m.PaymentSuccessComponent) },
    //profile
    { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
    { path: 'itinerary', component: ItineraryComponent },
  { path: 'itinerary-booking', component: ItineraryBookingComponent },
  { path: '', redirectTo: '/itinerary', pathMatch: 'full' },
    { path: '', redirectTo: '/itinerary', pathMatch: 'full' },
   //Unauthorized access
   { path: 'unauthorized', component: UnauthorizedComponent },
  // Wildcard: any unknown URL redirects to Home
  { path: '**', redirectTo: 'Home' }
 
];
 
 