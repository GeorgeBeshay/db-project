import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { Pet } from '../Entities/Pet';

@Injectable({
  providedIn: 'root'
})
export class StaffServicesService {

  petURL = 'http://localhost:8081/pasms-server/pet-api/'

  constructor(private http: HttpClient) { }

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
}
