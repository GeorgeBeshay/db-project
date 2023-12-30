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
import {Admin} from "../../Entities/Admin";
import {Adopter} from "../../Entities/Adopter";


@Component({
  selector: 'app-adopter',
  templateUrl: './adopter.component.html',
  styleUrls: ['./adopter.component.css']
})
export class AdopterComponent implements OnInit{

  adopter: Adopter | null = null;
  currentSection: number
  adopterService: AdopterServicesService
  utilitiesService!: UtilitiesService;
  adoptionApplications : AdoptionApplication[]
  applicationNotifications: ApplicationNotification[]
  petAvailabilityNotifications: PetAvailabilityNotification[]
  applicationForm: FormGroup;
  petId = 0
  adopterId = 200
  signInForm!: FormGroup;
  signUpForm!:FormGroup;

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

    this.signInForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    this.signUpForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      password: ['', [Validators.required, Validators.minLength(4)]],
      birthDate: ['', Validators.required],
      gender: [false, Validators.required],
      address: ['']
    });

    let tempObj = sessionStorage.getItem("adopterObject");
    if(tempObj != null) {
      this.adopter = JSON.parse(tempObj);
      this.selectSection(0);
      if (this.adopter?.id)
        this.adopterId = this.adopter?.id;
    }

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

  async signIn() {

    const email = this.signInForm.get('email')?.value;
    const password = this.signInForm.get('password')?.value;

    let tempAdopter = new Adopter(undefined, undefined, undefined, email, undefined, password, undefined, undefined, undefined);

    this.adopter = await this.adopterService.signIn(tempAdopter)

    if (this.adopter != null) {
      sessionStorage.clear();
      sessionStorage.setItem("adopterObject", JSON.stringify(this.adopter));
      await this.utilitiesService.sweetAlertSuccess("Successful Authentication.")
      this.selectSection(0);
      if (this.adopter?.id)
        this.adopterId = this.adopter?.id;
    } else {
      await this.utilitiesService.sweetAlertFailure("Authentication Failed.")
    }

  }

  async signUp() {

    const email = this.signUpForm.get('email')?.value;
    const password = this.signUpForm.get('password')?.value;
    const firstName = this.signUpForm.get('firstName')?.value;
    const lastName = this.signUpForm.get('lastName')?.value;
    const phone = this.signUpForm.get('phone')?.value;
    const birthDate = this.signUpForm.get('birthDate')?.value;
    const gender = this.signUpForm.get('gender')?.value;
    const address = this.signUpForm.get('address')?.value;


    let tempAdopter = new Adopter(undefined, firstName, lastName, email, phone, password, birthDate, gender, address);
    console.log(tempAdopter);
    this.adopter = await this.adopterService.signUp(tempAdopter)

    if (this.adopter != null) {
      sessionStorage.clear();
      sessionStorage.setItem("adopterObject", JSON.stringify(this.adopter));
      await this.utilitiesService.sweetAlertSuccess("Successful Authentication.")
      this.selectSection(0);
      if (this.adopter?.id)
        this.adopterId = this.adopter?.id;
    } else {
      await this.utilitiesService.sweetAlertFailure("Authentication Failed.")
    }

  }

  signOut() {
    this.adopter = null;
    sessionStorage.removeItem("adopterObject");
  }

  async loadApplications() {
    if(this.adopter?.id)
      this.adoptionApplications = await this.adopterService.fetchApplications(this.adopter?.id)
  }

  async loadAppNotifications() {
    if(this.adopter?.id)
      this.applicationNotifications = await this.adopterService.fetchAppNotifications(this.adopter?.id)
  }

  async loadPetNotifications() {
    if(this.adopter?.id)
      this.petAvailabilityNotifications = await this.adopterService.fetchPetNotifications(this.adopter?.id)
  }

}
