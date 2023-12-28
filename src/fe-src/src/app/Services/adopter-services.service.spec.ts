import { TestBed } from '@angular/core/testing';

import { AdopterServicesService } from './adopter-services.service';

describe('AdopterServicesService', () => {
  let service: AdopterServicesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdopterServicesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
