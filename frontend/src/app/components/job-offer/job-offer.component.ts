import { Component, OnInit } from '@angular/core';
import { JobOffer } from 'src/app/models/job-offer.model';
import { JobOfferService } from 'src/app/services/job-offer.service';

@Component({
  selector: 'app-job-offer',
  templateUrl: './job-offer.component.html',
  styleUrls: ['./job-offer.component.css']
})
export class JobOfferComponent implements OnInit {
  jobOffers: JobOffer[] = [];
  constructor(private jobOfferService: JobOfferService) {}

  ngOnInit(): void {
    this.jobOfferService.getJobOffers().subscribe((data) => {
      this.jobOffers = data;
    });
  }

}
