import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdopterInfoComponent } from './adopter-info.component';

describe('AdopterInfoComponent', () => {
  let component: AdopterInfoComponent;
  let fixture: ComponentFixture<AdopterInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdopterInfoComponent]
    });
    fixture = TestBed.createComponent(AdopterInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
