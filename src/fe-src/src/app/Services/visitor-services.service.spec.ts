import { TestBed } from '@angular/core/testing';

import { VisitorServicesService } from './visitor-services.service';

describe('VisitorServicesService', () => {
  let service: VisitorServicesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VisitorServicesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
