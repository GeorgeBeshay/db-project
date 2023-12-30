import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Pet } from '../Entities/Pet';

@Injectable({
  providedIn: 'root'
})
export class VisitorServicesService {

  visitorURL = 'http://localhost:8081/pasms-server/visitor-api/';


  constructor(private http: HttpClient) { }

  //  ---------------------------- Service Methods ----------------------------

  async getSearchedAndSortedPets(criteria: object, shelterLocation: string, orderedByColumns: string[]) {
    let requestedMap = {
      'criteria': criteria,
      'shelterLocation': shelterLocation,
      'orderedByColumns': orderedByColumns
    };

    try {
      return await firstValueFrom (
        this.http.post<Pet[]>(this.visitorURL + 'getSearchedAndSortedPets', requestedMap, {responseType:'json'})
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return null;
  }
}
