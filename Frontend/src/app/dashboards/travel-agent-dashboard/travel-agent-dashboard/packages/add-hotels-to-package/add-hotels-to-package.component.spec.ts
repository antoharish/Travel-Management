import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddHotelsToPackageComponent } from './add-hotels-to-package.component';

describe('AddHotelsToPackageComponent', () => {
  let component: AddHotelsToPackageComponent;
  let fixture: ComponentFixture<AddHotelsToPackageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddHotelsToPackageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddHotelsToPackageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
