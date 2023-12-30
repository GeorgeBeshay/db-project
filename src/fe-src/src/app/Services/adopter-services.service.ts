import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { AdoptionApplication } from './../Entities/AdoptionApplication';
import { Admin } from '../Entities/Admin';
import { Adopter } from '../Entities/Adopter';
import { ApplicationNotification } from '../Entities/ApplicationNotification';
import { PetAvailabilityNotification } from '../Entities/PetAvailabilityNotification';

@Injectable({
  providedIn: 'root'
})

export class AdopterServicesService {

  baseUrl = 'http://localhost:8081/pasms-server/adopter-api/'

  constructor(private http: HttpClient) { }

  async submitApplication(application: AdoptionApplication) {
    try {
      return await firstValueFrom(this.http.post<number>(
        this.baseUrl + 'submit-application', application,
        {responseType: 'json'})
        );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Submitting adoption application failed');
      else
        console.error('Unexpected error occured')
      return -1;
    }
  }

  async fetchApplications(adopterId: number) {
    try {
      return await firstValueFrom(this.http.get<AdoptionApplication[]>(
        this.baseUrl + 'fetch-applications/' + adopterId,
        {responseType: 'json'})
        );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Fetching adoption applications failed for adopter ' + adopterId);
      else
        console.error('Unexpected error occured')
      return [];
    }
  }

  async getById(adopterId: number) {
    try {
      return await firstValueFrom(this.http.get<Adopter>(
        this.baseUrl + 'get-by-id/' + adopterId,
        {responseType: 'json'})
        );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Fetching failed for info of adopter ' + adopterId);
      else
        console.error('Unexpected error occured')
      return null;
    }
  }

  async signIn(adopter: Adopter) {

    try {
      return await firstValueFrom (
        this.http.post<Adopter>(`${this.baseUrl}sign-in`, adopter, {responseType:'json'})
      );

    } catch (error) {
      console.error(error instanceof HttpErrorResponse ? 'Bad request' : 'Error');
      return null

    }


  }

  async signUp(adopter: Adopter) {

    try {
      return await firstValueFrom(
        this.http.post<Adopter>(`${this.baseUrl}sign-up`, adopter, {responseType: 'json'})
      );

    } catch (error) {
      console.error(error instanceof HttpErrorResponse ? 'Bad request' : 'Error');
      return null

    }

  }

  async fetchAppNotifications(adopterId: number) {
    try {
      return await firstValueFrom(this.http.get<ApplicationNotification[]>(
        this.baseUrl + 'fetch-app-notifications/' + adopterId, 
        {responseType: 'json'})
        );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Fetching application notifications failed for adopter ' + adopterId);
      else
        console.error('Unexpected error occured')
      return [];
    }
  }

  async fetchPetNotifications(adopterId: number) {
    try {
      return await firstValueFrom(this.http.get<PetAvailabilityNotification[]>(
        this.baseUrl + 'fetch-pet-notifications/' + adopterId, 
        {responseType: 'json'})
        );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Fetching pet availability notifications failed for adopter ' + adopterId);
      else
        console.error('Unexpected error occured')
      return [];
    }
  }

}
