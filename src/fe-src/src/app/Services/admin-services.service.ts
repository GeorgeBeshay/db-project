import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {firstValueFrom} from "rxjs";
import {Admin} from "../Entities/Admin";
import {Staff} from "../Entities/Staff";

@Injectable({
  providedIn: 'root'
})
export class AdminServicesService {
  //  ---------------------------- Component Fields ----------------------------
  adminEndpointUrl = "http://localhost:8081/pasms-server/admin-api/"

  //  ---------------------------- Constructor ----------------------------
  constructor(private http: HttpClient) { }

  //  ---------------------------- Service Methods ----------------------------
  async adminSignIn(admin: Admin) {

    try {
      return await firstValueFrom (
        this.http.post<Admin>(`${this.adminEndpointUrl}adminSignIn`, admin, {responseType:'json'})
      );

    } catch (error) {
      console.error(error instanceof HttpErrorResponse ? 'Bad request' : 'Error');
      return null

    }

  }

  async createStaff(staff: Staff) {

    try {
      return await firstValueFrom (
        this.http.post<Staff>(`${this.adminEndpointUrl}createStaff`, staff, {responseType:'json'})
      );

    } catch (error) {
      console.error(error instanceof HttpErrorResponse ? 'Bad request' : 'Error');
      return null;

    }
  }

}
