import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./Components/home/home.component";
import {AboutComponent} from "./Components/about/about.component";
import {StaffComponent} from "./Components/staff/staff.component";
import {AdopterComponent} from "./Components/adopter/adopter.component";
import {AdminComponent} from "./Components/admin/admin.component";
import {VisitorComponent} from "./Components/visitor/visitor.component";
import { AdopterInfoComponent } from './Components/adopter-info/adopter-info.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'about', component: AboutComponent },
  { path: 'visitor', component: VisitorComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'adopter', component: AdopterComponent },
  { path: 'staff', component: StaffComponent },
  { path: 'adopter/:adopterId', component: AdopterInfoComponent },
  // Define routes for other components similarly
  { path: '', redirectTo: '/home', pathMatch: 'full' }, // Redirect to home by default
  { path: '**', redirectTo: '/home' } // Handle unknown routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
