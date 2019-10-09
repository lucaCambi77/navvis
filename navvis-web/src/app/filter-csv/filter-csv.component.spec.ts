import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterCsvComponent } from './filter-csv.component';

describe('FilterCsvComponent', () => {
  let component: FilterCsvComponent;
  let fixture: ComponentFixture<FilterCsvComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilterCsvComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterCsvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
