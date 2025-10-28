import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItineraryBookingComponent } from './itinerary-booking.component';

describe('ItineraryBookingComponent', () => {
  let component: ItineraryBookingComponent;
  let fixture: ComponentFixture<ItineraryBookingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItineraryBookingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItineraryBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
