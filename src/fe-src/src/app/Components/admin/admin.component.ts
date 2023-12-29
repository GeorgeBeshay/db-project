import {Component, OnInit} from '@angular/core';
import {Admin} from "../../Entities/Admin";
import {FormBuilder, FormGroup, Validators } from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {AdminServicesService} from "../../Services/admin-services.service";

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

  //  ---------------------------- Constructor ----------------------------
  constructor(private formBuilder: FormBuilder, private http: HttpClient) {
    this.adminService = new AdminServicesService(http);
  }

  ngOnInit() {
    this.signInForm = this.formBuilder.group({
      id: ['', [Validators.required]],
      password: ['', Validators.required]
    });
  }

  //  ---------------------------- Component Methods ----------------------------
  async signIn() {
    if (this.signInForm.valid) {

      const id = this.signInForm.get('id')?.value;
      const password = this.signInForm.get('password')?.value;
      let tempAdmin = new Admin(id, undefined, undefined, undefined, undefined, password);

      this.admin = await this.adminService.adminSignIn(tempAdmin)
    } else {
      // Form is invalid
      console.log('Invalid form submission');
    }
    console.log("HERE");

  }

}
