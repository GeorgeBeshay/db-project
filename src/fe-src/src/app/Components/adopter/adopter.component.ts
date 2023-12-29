import { AdoptionApplication } from './../../Entities/AdoptionApplication';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ApplicationStatus } from 'src/app/Entities/ApplicationStatus';
import { AdopterServicesService } from 'src/app/Services/adopter-services.service';

@Component({
  selector: 'app-adopter',
  templateUrl: './adopter.component.html',
  styleUrls: ['./adopter.component.css']
})
export class AdopterComponent implements OnInit{

  currentSection: number
  adopterService: AdopterServicesService

  constructor(private http: HttpClient) {
    this.currentSection = 0
    this.adopterService = new AdopterServicesService(http);
  }

  ngOnInit(): void {
      
  }

  async selectSection(sectionNumber: number) {
    this.currentSection = sectionNumber
    console.log("Navigating to section", sectionNumber);
    if(this.currentSection == 1) { // Submit new application
      // Open form 
      // Send app data to BE when submitted
      let app = new AdoptionApplication(233, 1231, 3231, ApplicationStatus.Pending, 'Help!', false, '2023-12-29'); // replaced with fom data
      let result = await this.adopterService.submitApplication(app)
      console.log('Recieved', result)
    } else if (this.currentSection == 2) { // Fetch adoption applications for currernt adopter
      const adopterId = 11 // Replace with id of current user
      let apps = await this.adopterService.fetchApplications(adopterId)
      console.log('Recieved', apps)
    }
  }
}
