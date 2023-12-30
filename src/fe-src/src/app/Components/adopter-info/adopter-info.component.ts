import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdopterServicesService } from 'src/app/Services/adopter-services.service';
import { Adopter } from './../../Entities/Adopter';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-adopter-info',
  templateUrl: './adopter-info.component.html',
  styleUrls: ['./adopter-info.component.css']
})
export class AdopterInfoComponent implements OnInit {
  adopterId: string | any; 
  adopterInfo: Adopter | any;
  adopterService: AdopterServicesService

  constructor(private http: HttpClient,
    private route: ActivatedRoute
  ) { this.adopterService = new AdopterServicesService(http)}

  async ngOnInit() {
    // Get the adopter ID from the route parameter
    this.route.params.subscribe(params => {
      this.adopterId = params['adopterId'];
      this.loadAdopterInfo();
    });
  }

  async loadAdopterInfo() {
    // Use the AdopterService to fetch adopter information based on the adopter ID
    this.adopterService.getById(this.adopterId).then((data) => this.adopterInfo = data)
  }
}
