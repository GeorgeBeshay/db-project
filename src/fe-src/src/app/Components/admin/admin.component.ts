import {Component, OnInit} from '@angular/core';
import {Admin} from "../../Entities/Admin";
import {FormBuilder, FormGroup, Validators } from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {AdminServicesService} from "../../Services/admin-services.service";
import {UtilitiesService} from "../../Services/utilities.service";
import { Shelter } from 'src/app/Entities/Shelter';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  //  ---------------------------- Component Fields ----------------------------
  admin: Admin | null = null;
  signInForm!: FormGroup;
  adminService!: AdminServicesService;
  utilitiesService!: UtilitiesService;
  selectedSection!: number;
  shelterCreationForm!: FormGroup;
  systemShelters: Shelter[] | null = null;
  //  ---------------------------- Constructor ----------------------------
  constructor(private formBuilder: FormBuilder, private http: HttpClient) {
    this.adminService = new AdminServicesService(http);
    this.utilitiesService = new UtilitiesService();
  }

  ngOnInit() {
    this.signInForm = this.formBuilder.group({
      id: ['', [Validators.required]],
      password: ['', Validators.required]
    });
    this.shelterCreationForm = this.formBuilder.group({
      shelterName:['',Validators.required],
      shelterLocation:['',Validators.required],
      shelterEmail:['',Validators.required],
      shelterPhone:['',Validators.required],
      shelterManager:['',Validators.required]
    });

    let tempObj = sessionStorage.getItem("adminObject");
    if(tempObj != null) {
      this.admin = JSON.parse(tempObj);
      this.selectSection(0);
    }

  }

  //  ---------------------------- Component Methods ----------------------------
  async signIn() {

    const id = this.signInForm.get('id')?.value;
    const password = this.signInForm.get('password')?.value;
    let tempAdmin = new Admin(id, undefined, undefined, undefined, undefined, password);
    this.admin = await this.adminService.adminSignIn(tempAdmin)

    if (this.admin != null) {
      sessionStorage.clear();
      sessionStorage.setItem("adminObject", JSON.stringify(this.admin));
      await this.utilitiesService.sweetAlertSuccess("Successful Authentication.")
      this.selectSection(0);
    } else {
      await this.utilitiesService.sweetAlertFailure("Authentication Failed.")
    }

  }
  async createShelter()
  {
    const name = this.shelterCreationForm.get('shelterName')?.value;
    const location = this.shelterCreationForm.get('shelterLocation')?.value;
    const email = this.shelterCreationForm.get('shelterEmail')?.value;
    const phone = this.shelterCreationForm.get('shelterPhone')?.value;
    const manager = this.shelterCreationForm.get('shelterManager')?.value;
    let shelter = new Shelter(0,name,location,email,phone,manager);
    console.log(shelter);
    await this.adminService.createShelter(shelter);
  }

  selectSection(sectionNumber: number) {
    this.selectedSection = sectionNumber;
    console.log(this.selectedSection)
  }
  async getAllAvailableShelters(){
    this.systemShelters = await this.adminService.getAllAvailableShelters();
  }
}
