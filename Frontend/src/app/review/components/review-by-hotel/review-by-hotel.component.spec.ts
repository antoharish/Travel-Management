import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewByHotelComponent } from './review-by-hotel.component';

describe('ReviewByHotelComponent', () => {
  let component: ReviewByHotelComponent;
  let fixture: ComponentFixture<ReviewByHotelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewByHotelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewByHotelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
