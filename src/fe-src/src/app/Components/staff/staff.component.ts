import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { StaffServicesService } from '../../Services/staff-services.service'
import { Pet } from '../../Entities/Pet'
import {FormArray, FormControl, FormGroup, NonNullableFormBuilder, Validators, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.css']
})
export class StaffComponent implements OnInit{

  selectedSection: number
  staffService: StaffServicesService

  petForm: FormGroup
  availablePetsForAdoption: Pet[] = [];
  selectedPet: Pet | null = null;

  constructor(private http: HttpClient, private formBuilder: NonNullableFormBuilder) {
    this.selectedSection = 0
    this.staffService = new StaffServicesService(http);

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
  }

  async selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    console.log(this.selectedSection)
  }

  async onCreatePet() {
    console.log(this.petForm.value);
    const pet = this.petForm.value as Pet
    let result = await this.staffService.createPet(pet)
    this.petForm.patchValue({ id: result });
    console.log(result)
    this.loadAvailablePetsForAdoption()
  }

  async onUpdatePet() {
    console.log(this.petForm.value);
    const pet = this.petForm.value as Pet
    let result = await this.staffService.updatePet(pet)
    console.log(result)
    this.loadAvailablePetsForAdoption()
  }

  async onDeletePet() {
    console.log(this.petForm.value);
    const pet = this.petForm.value as Pet
    let result = await this.staffService.deletePet(pet)
    console.log(result)
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

}
