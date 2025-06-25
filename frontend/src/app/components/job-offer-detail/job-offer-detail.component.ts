import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApplicationCreateDTO } from 'src/app/models/ApplicatioCreateDTO.dto';
import { Application } from 'src/app/models/application.model';
import { JobOffer } from 'src/app/models/job-offer.model';
import { ApplicationService } from 'src/app/services/application.service';
import { AuthServiceService } from 'src/app/services/auth-service.service';
import { JobOfferService } from 'src/app/services/job-offer.service';

@Component({
  selector: 'app-job-offer-detail',
  templateUrl: './job-offer-detail.component.html',
  styleUrls: ['./job-offer-detail.component.css']
})
export class JobOfferDetailComponent implements OnInit {
  jobOffer!: JobOffer;
  application: Application = {
    id: null,
    applicationDate: new Date(),
    cvPath: '',
    jobOfferId: null,
    userId: null
  };
  selectedFile: File | null = null;

  constructor(
    private route: ActivatedRoute,
    private jobOfferService: JobOfferService,
private applicationService: ApplicationService,
    private authService: AuthServiceService

  ) {}

  ngOnInit(): void {
  console.log('Initializing component...');
  const id = this.route.snapshot.paramMap.get('id');
  console.log('Retrieved job offer ID from route:', id);
  if (id) {
    this.jobOfferService.getJobOfferById(+id).subscribe(
      (data) => {
        console.log('Retrieved job offer:', data);
        this.jobOffer = data;
        this.application.jobOfferId = this.jobOffer.id ?? null;
        console.log('Set jobOfferId in application:', this.application.jobOfferId);
      },
      (error) => {
        console.error('Error retrieving job offer:', error);
      }
    );
  }

  const userEmail = this.authService.getEmail(); // Assuming you have this method
  console.log('Retrieved user email from AuthService:', userEmail);
  if (userEmail) {
    this.authService.getUserIdByEmail(userEmail).subscribe(
      (userId) => {
        console.log('Retrieved user ID:', userId);
        this.application.userId = userId; // Set User ID
        console.log('Set userId in application:', this.application.userId);
      },
      (error) => {
        console.error('Error retrieving user ID:', error);
      }
    );
  }
}

onFileSelected(event: any): void {
  this.selectedFile = event.target.files[0];
  console.log('File selected:', this.selectedFile);
  if (this.selectedFile) {
    this.application.cvPath = this.selectedFile.name;
    console.log('Set cvPath in application:', this.application.cvPath);
  }
}


onSubmit(): void {
  if (this.selectedFile) {
    const applicationToSend: ApplicationCreateDTO = {
      applicationDate: this.application.applicationDate,
      cvPath: this.application.cvPath
    };

    this.applicationService.uploadApplication(
      this.application.jobOfferId!,   // Non-null assertion because you set it before
      this.application.userId!,       // Same here
      applicationToSend,
      this.selectedFile
    ).subscribe(
      response => {
        console.log('Application submitted successfully:', response);
        alert('Application submitted successfully!');
      },
      error => {
        console.error('Error submitting application:', error);
        alert('Failed to submit application.');
      }
    );
  } else {
    alert('Please upload a CV before submitting.');
  }
}

}