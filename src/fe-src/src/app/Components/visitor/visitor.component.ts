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

  visitorService: VisitorServicesService;

  constructor(private http: HttpClient) {
    this.visitorService = new VisitorServicesService(http);
  }

  async ngOnInit() {
    let criteria = {
      "specie": "dog",
      "gender": true
    };

    let shelterLocation = "";

    let orderedByColumns = ["neutering"];

    let result = await this.visitorService.getSearchedAndSortedPets(criteria, shelterLocation, orderedByColumns);
    console.log(result);
  }

}
