import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFlightToPackageComponent } from './add-flight-to-package.component';

describe('AddFlightToPackageComponent', () => {
  let component: AddFlightToPackageComponent;
  let fixture: ComponentFixture<AddFlightToPackageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddFlightToPackageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddFlightToPackageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
