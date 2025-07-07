import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Application } from '../models/application.model';
import { AuthServiceService } from './auth-service.service';
import { ApplicationCreateDTO } from '../models/ApplicatioCreateDTO.dto';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

 private apiUrl = 'http://192.168.49.2:30089/PGH/api/applications';

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
 
uploadApplication(
  jobOfferId: number,
  userId: number,
  application: ApplicationCreateDTO,
  file: File
): Observable<Application> {
  const token = this.authService.getToken();
  if (!token) {
    throw new Error('No token found');
  }

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  const formData = new FormData();
  formData.append('application', JSON.stringify(application));
  formData.append('file', file);

  return this.http.post<Application>(
    `${this.apiUrl}/create/${jobOfferId}/${userId}`,
    formData,
    { headers }
  );
}}
