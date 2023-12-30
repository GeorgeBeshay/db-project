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

  pets: any[] | null = [];

  selectedDocuments: PetDocument[] | null = [];
  currentDocument: PetDocument | null = null;
  selectedPetView: Pet = new Pet(0, "", "", "", "", false, "", "", "", 1);

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
  }

  async ngOnInit() {
    // let criteria = {
    //   "specie": "dog",
    //   "gender": true
    // };

    // let shelterLocation = "";

    // let orderedByColumns = ["neutering"];

    // let result = await this.visitorService.getSearchedAndSortedPets(criteria, shelterLocation, orderedByColumns);
    // console.log(result);
  }

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
      // Handle the error as needed (e.g., show an error message to the user)
    }
  }

  async onSelectPetView(pet: Pet) {
    this.selectedPetView = pet;
    this.selectedDocuments = await this.visitorService.findByPetId(pet.id);
  }


}
