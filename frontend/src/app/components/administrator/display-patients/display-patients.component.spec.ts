import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayPatientsComponent } from './display-patients.component';

describe('DisplayPatientsComponent', () => {
  let component: DisplayPatientsComponent;
  let fixture: ComponentFixture<DisplayPatientsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DisplayPatientsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayPatientsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
