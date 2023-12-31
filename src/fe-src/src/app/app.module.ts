import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './Components/home/home.component';
import { AboutComponent } from './Components/about/about.component';
import { VisitorComponent } from './Components/visitor/visitor.component';
import { AdminComponent } from './Components/admin/admin.component';
import { AdopterComponent } from './Components/adopter/adopter.component';
import { StaffComponent } from './Components/staff/staff.component';

import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { AdopterInfoComponent } from './Components/adopter-info/adopter-info.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AboutComponent,
    VisitorComponent,
    AdminComponent,
    AdopterComponent,
    StaffComponent,
    AdopterInfoComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
      ReactiveFormsModule,
      HttpClientModule,
      DatePipe,
    ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
