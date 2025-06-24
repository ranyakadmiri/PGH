import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { JobOfferComponent } from './components/job-offer/job-offer.component';
import { JobOfferDetailComponent } from './components/job-offer-detail/job-offer-detail.component';

const routes: Routes = [{ path: '', component: HomeComponent },{ path: 'jobs/:id', component: JobOfferDetailComponent },
  { path: 'login', component: LoginComponent }, { path: 'jobs', component: JobOfferComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
