import { PetAvailabilityNotification } from './../../Entities/PetAvailabilityNotification';
import { ApplicationNotification } from './../../Entities/ApplicationNotification';
import { AdoptionApplication } from './../../Entities/AdoptionApplication';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ApplicationStatus } from 'src/app/Entities/ApplicationStatus';
import { AdopterServicesService } from 'src/app/Services/adopter-services.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import {UtilitiesService} from "../../Services/utilities.service";


@Component({
  selector: 'app-adopter',
  templateUrl: './adopter.component.html',
  styleUrls: ['./adopter.component.css']
})
export class AdopterComponent implements OnInit{

  // adopter: Adopter | null; 
  currentSection: number
  adopterService: AdopterServicesService
  utilitiesService!: UtilitiesService;
  adoptionApplications : AdoptionApplication[]
  applicationNotifications: ApplicationNotification[]
  petAvailabilityNotifications: PetAvailabilityNotification[]
  applicationForm: FormGroup;
  adopterId = 200

  constructor(private http: HttpClient, private formBuilder: FormBuilder, private datePipe: DatePipe) {
    this.currentSection = 0
    this.adopterService = new AdopterServicesService(http);
    this.utilitiesService = new UtilitiesService();
    this.applicationForm = this.formBuilder.group({
      adopterId: [this.adopterId, Validators.required],
      petId: [0, Validators.required],
      description: ['', Validators.required],
      experience: [false] // Default value for the checkbox
    });
    this.adoptionApplications = []
    this.applicationNotifications = []
    this.petAvailabilityNotifications = []
  }

  ngOnInit(): void {
    this.loadApplications()
    this.loadAppNotifications()
    this.loadPetNotifications()
  }

  async selectSection(sectionNumber: number) {
    this.currentSection = sectionNumber
    console.log("Navigating to section", sectionNumber);
  }

  async submitApplication() {
    if(this.applicationForm.valid) {
      console.log('Adoption application submitted:', this.applicationForm.value);

      // Send to back
      const date = this.datePipe.transform(new Date(), 'yyyy-MM-dd')!;
      const app = new AdoptionApplication(0, this.applicationForm.value.adopterId, this.applicationForm.value.petId, ApplicationStatus.Pending, 
        this.applicationForm.value.description, this.applicationForm.value.experience, date);
      const result = await this.adopterService.submitApplication(app)

      if(result > 0) {
        await this.utilitiesService.sweetAlertSuccess("Successful Submission.")
      } else {
        await this.utilitiesService.sweetAlertFailure("Failed Submission.")
      }
      this.applicationForm.reset();
    } else {
      console.error('Adoption application form not valid')
    }
  }

  async loadApplications() {
    this.adoptionApplications = await this.adopterService.fetchApplications(this.adopterId)
  }

  async loadAppNotifications() {
    this.applicationNotifications = await this.adopterService.fetchAppNotifications(this.adopterId)
  }

  async loadPetNotifications() {
    this.petAvailabilityNotifications = await this.adopterService.fetchPetNotifications(this.adopterId)
  }
}
