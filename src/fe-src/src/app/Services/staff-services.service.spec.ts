import { TestBed } from '@angular/core/testing';

import { StaffServicesService } from './staff-services.service';

describe('StaffServicesService', () => {
  let service: StaffServicesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StaffServicesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
