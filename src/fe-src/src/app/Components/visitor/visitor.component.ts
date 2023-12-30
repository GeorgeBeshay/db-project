import {Component, OnInit} from '@angular/core';
import {UtilitiesService} from "../../Services/utilities.service";
import { Pet } from '../../Entities/Pet';
import { HttpClient } from '@angular/common/http';
import { VisitorServicesService } from 'src/app/Services/visitor-services.service';

@Component({
  selector: 'app-visitor',
  templateUrl: './visitor.component.html',
  styleUrls: ['./visitor.component.css']
})
export class VisitorComponent implements OnInit{

  pets: any[] | null = [];

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

      if (param === 'gender') {
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

}
