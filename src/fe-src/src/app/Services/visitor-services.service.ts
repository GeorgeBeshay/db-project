import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Pet } from '../Entities/Pet';
import { PetDocument } from '../Entities/PetDocument';

@Injectable({
  providedIn: 'root'
})
export class VisitorServicesService {

  visitorURL = 'http://localhost:8081/pasms-server/visitor-api/';
  petDocumentURL = `http://localhost:8081/pasms-server/pet-document-api/`;


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
    return [];
  }

  async downloadFile(documentId: number) {
    try {
      return await firstValueFrom (
        this.http.get(`${this.petDocumentURL}download/${documentId}`, {responseType:'blob'})
      );

    } catch (error) {
      console.error(error instanceof HttpErrorResponse ? 'Bad request' : 'Error');
      throw new Error('Error downloading document');
    }
  }

  async findByPetId(petId: number) {
    try {
      return await firstValueFrom (
        this.http.get<PetDocument[]>(`${this.petDocumentURL}findAllDocuments/${petId}`, {responseType:'json'})
      );

    } catch (error) {
      console.error(error instanceof HttpErrorResponse ? 'Bad request' : 'Error');
      return [];
    }
  }
}
