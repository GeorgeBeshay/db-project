import {Component, OnInit} from '@angular/core';
import {UtilitiesService} from "../../Services/utilities.service";
import { Pet } from '../../Entities/Pet';
import { PetDocument } from 'src/app/Entities/PetDocument';
import { HttpClient } from '@angular/common/http';
import { VisitorServicesService } from 'src/app/Services/visitor-services.service';

@Component({
  selector: 'app-visitor',
  templateUrl: './visitor.component.html',
  styleUrls: ['./visitor.component.css']
})
export class VisitorComponent implements OnInit{

  pets: Pet[] = [];
  utilitiesService: UtilitiesService;

  selectedDocuments: PetDocument[] = [];
  currentDocument: PetDocument | null = null;
  selectedPetView: Pet | null= null;

  visitorService: VisitorServicesService;
  searchParams: any = {
    specie: '',
    breed: '',
    birthdate: '',
    gender: '',
    shelterLocation: ''
  };

  sorting: any = {
    neutering: false,
    house_training: false,
    vaccination: false
  };

  constructor(private http: HttpClient) {
    this.visitorService = new VisitorServicesService(http);
    this.utilitiesService = new UtilitiesService();
  }

  async ngOnInit() {}

  async search() {
    let criteria: { [key: string]: string | boolean } = {}
    let shelterLocation = this.searchParams.shelterLocation
    let orderedByColumns: string[] = [];

    for (let param in this.searchParams) {
      if (param === 'shelterLocation') {
        continue;
      }

      if (param === 'gender' && this.searchParams[param]) {
        if (this.searchParams[param] == 'Male') {
          criteria[param] = true;
        }
        else {
          criteria[param] = false;
        }
      } else if (this.searchParams[param]) {
        criteria[param] = this.searchParams[param];
      }
    }

    for (let sortParam in this.sorting) {
      if (this.sorting[sortParam]) {
        orderedByColumns.push(sortParam);
      }
    }

    console.log("criteria:", criteria);
    console.log("shelterLocation:", shelterLocation);
    console.log("orderedByColumns:", orderedByColumns);

    this.pets = await this.visitorService.getSearchedAndSortedPets(criteria, shelterLocation, orderedByColumns);
    console.log(this.pets);
  }

  async onDocumentSelected(petDocument: PetDocument) {
    this.currentDocument = petDocument;
    try {
      this.currentDocument = petDocument;
      const blob = await this.visitorService.downloadFile(petDocument.id);

      // Create a link element and trigger the download
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = petDocument.name;
      link.click();

    } catch (error) {
      console.error('Error downloading document', error);
      this.utilitiesService.sweetAlertFailure("Error in downloading the file.")
    }
  }

  async onSelectPetView(pet: Pet) {
    if (this.selectedPetView != null && this.selectedPetView.id === pet.id) {
      this.selectedPetView = null;
    }
    else {
      this.selectedPetView = pet;
      this.selectedDocuments = await this.visitorService.findByPetId(pet.id);
    }
  }


}
