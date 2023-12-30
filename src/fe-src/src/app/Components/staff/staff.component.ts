import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { StaffServicesService } from '../../Services/staff-services.service'
import {UtilitiesService} from "../../Services/utilities.service";
import { Pet } from '../../Entities/Pet'
import {FormArray, FormControl, FormGroup, NonNullableFormBuilder, Validators, ReactiveFormsModule} from "@angular/forms";
import {Staff} from "../../Entities/Staff";
import {Admin} from "../../Entities/Admin";

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.css']
})
export class StaffComponent implements OnInit{

  selectedSection: number
  staffService: StaffServicesService
  utilitiesService: UtilitiesService;
  staff: Staff | null = null;

  petForm: FormGroup
  availablePetsForAdoption: Pet[] = [];
  selectedPet: Pet | null = null;
  signInForm!: FormGroup;

  constructor(private http: HttpClient, private formBuilder: NonNullableFormBuilder) {
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

    this.signInForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  async ngOnInit() {
    await this.loadAvailablePetsForAdoption();

    let tempObj = sessionStorage.getItem("staffObject");
    if(tempObj != null) {
      this.staff = JSON.parse(tempObj);
      this.selectSection(0);
    }
  }

  selectSection (sectionIndex: number) {
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

    await this.loadAvailablePetsForAdoption()
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

    await this.loadAvailablePetsForAdoption()
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

    await this.loadAvailablePetsForAdoption()
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

  async signIn() {

    const email = this.signInForm.get('email')?.value;
    const password = this.signInForm.get('password')?.value;
    let tempStaff = new Staff(undefined, undefined, undefined, undefined, undefined, email, password, undefined);
    console.log(tempStaff);
    this.staff = await this.staffService.signIn(tempStaff)

    if (this.staff != null) {
      sessionStorage.clear();
      sessionStorage.setItem("staffObject", JSON.stringify(this.staff));
      await this.utilitiesService.sweetAlertSuccess("Successful Authentication.")
      this.selectSection(0);
    } else {
      await this.utilitiesService.sweetAlertFailure("Authentication Failed.")
    }

  }

  signOut() {
    this.staff = null;
    sessionStorage.removeItem("staffObject");
  }

}
