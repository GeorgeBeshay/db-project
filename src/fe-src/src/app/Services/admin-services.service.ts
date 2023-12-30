import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {firstValueFrom} from "rxjs";
import {Admin} from "../Entities/Admin";
import {Staff} from "../Entities/Staff";
import { Shelter } from '../Entities/Shelter';

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
  async createShelter(shelter: Shelter) {
    
    try {
      return await firstValueFrom(
      this.http.post<boolean>(`${this.adminEndpointUrl}createShelter`,shelter,{responseType:'json'})
      );
    } catch(error){
      if(error instanceof HttpErrorResponse)
        console.error('Bad Request');
      else
        console.error('Error');
      return null
    }
  }
  async getAllAvailableShelters(): Promise<Shelter[]> {
    try{ 
      return await firstValueFrom(this.http.post<Shelter[]>(`${this.adminEndpointUrl}getAllAvailableShelters`,{},{responseType:'json'}));
  }catch(error){
    if(error instanceof HttpErrorResponse){
      console.error('Bad Request');
    }
    console.error('Error');
  }
  return [];
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
