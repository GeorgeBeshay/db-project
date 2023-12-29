import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { StaffServicesService } from '../../Services/staff-services.service'
import { Pet } from '../../Entities/Pet'

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.css']
})
export class StaffComponent implements OnInit{

  selectedSection: number
  staffService: StaffServicesService

  constructor(private http: HttpClient) {
    this.selectedSection = 0
    this.staffService = new StaffServicesService(http);
  }

  ngOnInit() {

  }

  async selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    console.log(this.selectedSection)
    if (this.selectedSection == 1) {
      let pet = new Pet(77, "dog", "testing", "testing", "", true, "", "", "", 1);
      let result = await this.staffService.getUnAdoptedPets()
      console.log(result)
    }
  }

}
