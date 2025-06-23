import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { JobOfferComponent } from './components/job-offer/job-offer.component';

const routes: Routes = [{ path: 'home', component: HomeComponent },
  { path: '', component: LoginComponent }, { path: 'jobs', component: JobOfferComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
