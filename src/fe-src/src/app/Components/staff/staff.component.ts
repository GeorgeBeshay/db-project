import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { StaffServicesService } from '../../Services/staff-services.service'
import {UtilitiesService} from "../../Services/utilities.service";
import { Pet } from '../../Entities/Pet'
import {FormArray, FormControl, FormGroup, NonNullableFormBuilder, Validators, ReactiveFormsModule} from "@angular/forms";
import { AdoptionApplication } from 'src/app/Entities/AdoptionApplication';
import { ApplicationStatus } from 'src/app/Entities/ApplicationStatus';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.css']
})
export class StaffComponent implements OnInit{

  selectedSection: number
  staffService: StaffServicesService
  utilitiesService: UtilitiesService;

  petForm: FormGroup
  availablePetsForAdoption: Pet[] = [];
  adoptionApplications: AdoptionApplication[] = [];
  selectedPet: Pet | null = null;

  constructor(private http: HttpClient, private formBuilder: NonNullableFormBuilder, private datePipe: DatePipe) {
    this.selectedSection = 0
    this.staffService = new StaffServicesService(http);
    this.utilitiesService = new UtilitiesService();

    this.petForm = this.formBuilder.group({
      id: [],
      name: ['', Validators.required],
      specie: ['', Validators.required],
      breed: ['', Validators.required],
      birthdate: ['', Validators.required],
      gender: [false, Validators.required],
      healthStatus: ['', Validators.required],
      behaviour: ['', Validators.required],
      description: ['', Validators.required],
      shelterId: [null, Validators.required],
      neutering: [false],
      houseTraining: [false],
      vaccination: [false],
    });
  }

  async ngOnInit() {
    this.loadAvailablePetsForAdoption()
    this.loadApplications()
  }

  async selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    console.log(this.selectedSection)
  }

  async onCreatePet() {
    console.log(this.petForm.value);
    const pet = this.petForm.value as Pet
    let result = await this.staffService.createPet(pet)
    console.log(result)

    if (result > 0) {
      this.petForm.patchValue({ id: result });
      await this.utilitiesService.sweetAlertSuccess("Successful Pet Creation")
    }
    else {
      await this.utilitiesService.sweetAlertFailure("Pet Creation Failed")
    }

    this.loadAvailablePetsForAdoption()
  }

  async onUpdatePet() {
    console.log(this.petForm.value);
    const pet = this.petForm.value as Pet
    let result = await this.staffService.updatePet(pet)
    console.log(result)

    if (result) {
      await this.utilitiesService.sweetAlertSuccess("Pet updated successfully")
    }
    else {
      await this.utilitiesService.sweetAlertFailure("Pet can not be updated")
    }

    this.loadAvailablePetsForAdoption()
  }

  async onDeletePet() {
    console.log(this.petForm.value);
    const pet = this.petForm.value as Pet
    let result = await this.staffService.deletePet(pet)
    console.log(result)

    if(result) {
      await this.utilitiesService.sweetAlertSuccess("Pet is deleted successfully")
    }
    else {
      await this.utilitiesService.sweetAlertFailure("Pet can not be deleted")
    }
    
    this.loadAvailablePetsForAdoption()
  }

  async loadAvailablePetsForAdoption() {
    this.availablePetsForAdoption = await this.staffService.getUnAdoptedPets();
  }

  onSelectPet(pet: Pet) {
    this.selectedPet = pet;
    this.petForm.patchValue({
      id: pet.id,
      name: pet.name,
      specie: pet.specie,
      breed: pet.breed,
      birthdate: pet.birthdate,
      gender: pet.gender,
      healthStatus: pet.healthStatus,
      behaviour: pet.behaviour,
      description: pet.description,
      shelterId: pet.shelterId,
      neutering: pet.neutering,
      houseTraining: pet.houseTraining,
      vaccination: pet.vaccination
    });
  }

  async loadApplications() {
    this.adoptionApplications = await this.staffService.fetchApplications();
  }

  async updateApplication(adoptionApplication: AdoptionApplication,) {
    const confirmUpdate = window.confirm("Are you sure you want to update the application status ?")

    if(confirmUpdate) {
      adoptionApplication.closingDate = this.datePipe.transform(new Date(), 'yyyy-MM-dd')!;
      const result = await this.staffService.updateApplication(adoptionApplication)
      console.log(result)
    }
    
    this.loadApplications()
  }
}
