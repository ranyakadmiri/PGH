import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { JobOffer } from '../models/job-offer.model';
import { AuthServiceService } from './auth-service.service';




@Injectable({
  providedIn: 'root'
})
export class JobOfferService {

 private apiUrl = 'http://localhost:8089/PGH/api/joboffers'; // Update to your actual backend URL

  constructor(private http: HttpClient, private authService:AuthServiceService) {}
 
  getJobOffers(): Observable<JobOffer[]> {
     const token = this.authService.getToken();
      if (!token) {
    throw new Error('No token found');
  }
    const headers = new HttpHeaders({
      
    'Authorization': `Bearer ${token}`
  });
    return this.http.get<JobOffer[]>(`${this.apiUrl}/GetJobOffers`, { headers });
  }
  getJobOfferById(id: number): Observable<JobOffer> {
  const token = this.authService.getToken();
  if (!token) {
    throw new Error('No token found');
  }
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
  return this.http.get<JobOffer>(`${this.apiUrl}/GetJobOfferByid/${id}`, { headers });
}
createJobOffer(formData: FormData): Observable<JobOffer> {
   const token = this.authService.getToken();
  if (!token) {
    throw new Error('No token found');
  }
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
    return this.http.post<JobOffer>(`${this.apiUrl}/CreateJobOffer`, formData, { headers });
  }
  
}
