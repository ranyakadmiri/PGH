import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { JobOffer } from 'src/app/models/job-offer.model';
import { JobOfferService } from 'src/app/services/job-offer.service';

@Component({
  selector: 'app-job-offer',
  templateUrl: './job-offer.component.html',
  styleUrls: ['./job-offer.component.css']
})
export class JobOfferComponent implements OnInit {
   jobOffer: JobOffer = {
    title: '',
    description: '',
    requirements: '',
    location: '',
    postedDate: new Date(),
    status: true,
    imagePath:''
  };

  selectedImage: File | null = null;
  jobOffers: JobOffer[] = [];
  constructor(private jobOfferService: JobOfferService,  private sanitizer: DomSanitizer) {}
ngOnInit(): void {
  this.jobOfferService.getJobOffers().subscribe((data) => {
    this.jobOffers = data.map((job) => ({
      ...job,
      imagePath: this.sanitizeUrl(job.imagePath || 'assets/img/default-job.jpg')
    }));
  });
}

sanitizeUrl(imagePath: string): any {
  return this.sanitizer.bypassSecurityTrustUrl(imagePath);
}
  onFileSelected(event: any): void {
    this.selectedImage = event.target.files[0];
  }

  onSubmit(): void {
    const formData = new FormData();
    formData.append('jobOffer', JSON.stringify(this.jobOffer));
    if (this.selectedImage) {
      formData.append('image', this.selectedImage);
    }

    this.jobOfferService.createJobOffer(formData).subscribe(
      (response) => {
        console.log('Job offer created successfully:', response);
        alert('Job offer created successfully!');
      },
      (error) => {
        console.error('Error creating job offer:', error);
        alert('Failed to create job offer.');
      }
    );
  }

}
