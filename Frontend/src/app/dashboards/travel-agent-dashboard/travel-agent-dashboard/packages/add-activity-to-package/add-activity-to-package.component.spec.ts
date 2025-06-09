import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddActivityToPackageComponent } from './add-activity-to-package.component';

describe('AddActivityToPackageComponent', () => {
  let component: AddActivityToPackageComponent;
  let fixture: ComponentFixture<AddActivityToPackageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddActivityToPackageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddActivityToPackageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
