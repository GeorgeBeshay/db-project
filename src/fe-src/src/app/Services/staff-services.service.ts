import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Pet } from '../Entities/Pet';
import { AdoptionApplication } from '../Entities/AdoptionApplication';
import { ApplicationStatus } from '../Entities/ApplicationStatus';
import {Staff} from "../Entities/Staff";

@Injectable({
  providedIn: 'root'
})
export class StaffServicesService {

  petURL = 'http://localhost:8081/pasms-server/pet-api/'
  staffURL = 'http://localhost:8081/pasms-server/staff-api/'
  staffEndpointURL = 'http://localhost:8081/pasms-server/staff-api/';

  constructor(private http: HttpClient) { }

  //  ---------------------------- Service Methods ----------------------------

  async getUnAdoptedPets() {

    try {
      return await firstValueFrom (
        this.http.get<Pet[]>(this.petURL + 'getUnAdoptedPets', {responseType:'json'})
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return [];
  }

  async findPetById(petId: number) {

    try {
      return await firstValueFrom (
        this.http.post<Pet>(this.petURL + 'findPetById', petId, {responseType:'json'})
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return null;
  }

  async createPet(pet: Pet) {

    try {
      return await firstValueFrom (
        this.http.post<number>(this.petURL + 'createPet', pet, {responseType:'json'})
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return -1;
  }

  async updatePet(pet: Pet) {

    try {
      return await firstValueFrom (
        this.http.post<boolean>(this.petURL + 'updatePet', pet, {responseType:'json'})
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return false;
  }

  async deletePet(pet: Pet) {

    try {
      return await firstValueFrom (
        this.http.post<boolean>(this.petURL + 'deletePet', pet, {responseType:'json'})
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return false;
  }

  async fetchApplications() {
    try {
      return await firstValueFrom(
        this.http.get<AdoptionApplication[]>(this.staffURL + 'fetch-applications',
        {responseType:'json'})
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Error fetching adoption applications');

      else
        console.error('Error in client');
    }
    return []
  }

  async fetchApplicationsByStatus(status: ApplicationStatus) {
    try {
      let params = new HttpParams().set('status', status)
      return await firstValueFrom(
        this.http.get<AdoptionApplication[]>(this.staffURL + 'fetch-applications-by-status',
        {params: params, responseType:'json'})
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Error fetching adoption applications');

      else
        console.error('Error in client');
    }
    return []
  }

  async updateApplication(adoptionApplication: AdoptionApplication) {
    try {
      return await firstValueFrom(
        this.http.put<boolean>(this.staffURL + 'update-application',
        adoptionApplication,
        {responseType:'json'})
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Error updating adoption application');

      else
        console.error('Error in client');
    }
    return false
  }
  
  async updateStaff(staff: Staff) {

    try {
      return await firstValueFrom (
        this.http.post<Staff>(`${this.staffEndpointURL}updateStaff`, staff, {responseType:'json'})
      );

    } catch (error) {
      console.error(error instanceof HttpErrorResponse ? 'Bad request' : 'Error');
      return null

    }
  }
}
