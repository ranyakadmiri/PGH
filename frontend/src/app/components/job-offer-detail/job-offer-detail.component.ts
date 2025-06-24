import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JobOffer } from 'src/app/models/job-offer.model';
import { JobOfferService } from 'src/app/services/job-offer.service';

@Component({
  selector: 'app-job-offer-detail',
  templateUrl: './job-offer-detail.component.html',
  styleUrls: ['./job-offer-detail.component.css']
})
export class JobOfferDetailComponent implements OnInit {
  jobOffer!: JobOffer;

  constructor(
    private route: ActivatedRoute,
    private jobOfferService: JobOfferService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.jobOfferService.getJobOfferById(+id).subscribe((data) => {
        this.jobOffer = data;
      });
    }
  }
}