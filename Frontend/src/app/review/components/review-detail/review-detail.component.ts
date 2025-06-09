import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Review } from '../../../models/review.model';
import { ReviewService } from '../../../services/review.service';

@Component({
  selector: 'app-review-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './review-detail.component.html',
  styleUrls: ['./review-detail.component.css']
})
export class ReviewDetailComponent implements OnInit {
  review?: Review;

  constructor(
    private route: ActivatedRoute,
    private reviewService: ReviewService,
    private location: Location,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadReview(id);
  }

  loadReview(id: number): void {
    this.reviewService.getReviewById(id).subscribe({
      next: (data: Review) => this.review = data,
      error: err => console.error(err)
    });
  }

  goBack(): void {
    this.location.back();
  }
  
  editReview(): void {
    if (this.review && this.review.reviewID) {
      this.router.navigate(['/reviews/edit', this.review.reviewID]);
    }
  }

}
