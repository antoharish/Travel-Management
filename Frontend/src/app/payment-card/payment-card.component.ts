import { Component, Input } from '@angular/core';
import { HomePageComponent } from '../home-page/home-page.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-payment-card',
  imports: [CommonModule],
  templateUrl: './payment-card.component.html',
  styleUrls: ['./payment-card.component.css'],
  standalone: true // Mark the component as standalone
})
export class PaymentCardComponent {
  @Input() totalPrice: number = 0; // Input for the total price
  @Input() customizedActivities: any[] = []; // Input for the list of customized activities

  proceedToPayment(): void {
    console.log('Proceeding to payment...');
  }
}
