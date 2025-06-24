import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Application } from '../models/application.model';
import { AuthServiceService } from './auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

 private apiUrl = 'http://localhost:8089/PGH/api/applications';

  constructor(private http: HttpClient,private authService:AuthServiceService) {}

  createApplication(application: Application): Observable<Application> {
     const token = this.authService.getToken();
          if (!token) {
        throw new Error('No token found');
      }
        const headers = new HttpHeaders({
          
        'Authorization': `Bearer ${token}`
      });
    const { jobOfferId, userId } = application;
    return this.http.post<Application>(
      `${this.apiUrl}/create/${jobOfferId}/${userId}`,
      application, { headers }
    );
  }
}