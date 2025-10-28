// import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
// import { CommonModule } from '@angular/common';

// @Component({
//   selector: 'app-star-rating',
//   standalone: true,
//   imports: [CommonModule], // <-- include CommonModule here
//   templateUrl: './star-rating.component.html',
//   styleUrls: ['./star-rating.component.css']
// })
// export class StarRatingComponent implements OnChanges {
//   @Input() rating: number = 0;
//   @Input() maxRating: number = 5;
//   @Output() ratingChange: EventEmitter<number> = new EventEmitter<number>();

//   stars: number[] = [];

//   ngOnChanges(changes: SimpleChanges): void {
//     this.stars = Array.from({ length: this.maxRating }, (_, i) => i + 1);
//   }

//   setRating(value: number): void {
//     this.rating = value;
//     this.ratingChange.emit(this.rating);
//   }

//   onKeydown(event: KeyboardEvent, star: number): void {
//     if (event.key === 'Enter' || event.key === ' ') {
//       event.preventDefault();
//       this.setRating(star);
//     }
//   }
// }
import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-star-rating',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="star-rating" [class.readonly]="isReadOnly">
      @for (star of stars; track star) {
        <i class="fas fa-star" 
           [class.active]="star <= rating"
           [class.clickable]="!isReadOnly"
           (click)="!isReadOnly && setRating(star)">
        </i>
      }
    </div>
  `,
  styles: [`
    .star-rating {
      display: inline-flex;
      gap: 4px;
    }
    .fa-star {
      color: #ddd;
      font-size: 1.25rem;
    }
    .fa-star.active {
      color: #ffd700;
    }
    .fa-star.clickable {
      cursor: pointer;
    }
    .fa-star.clickable:hover {
      transform: scale(1.1);
    }
    .readonly .fa-star {
      cursor: default;
    }
  `]
})
export class StarRatingComponent implements OnChanges {
  @Input() rating: number = 0;
  @Input() maxRating: number = 5;
  @Input() isReadOnly: boolean = false;
  @Output() ratingChange = new EventEmitter<number>();

  stars: number[] = [];

  ngOnChanges(changes: SimpleChanges): void {
    this.stars = Array.from({ length: this.maxRating }, (_, i) => i + 1);
  }

  setRating(value: number): void {
    if (this.isReadOnly) return;
    this.rating = value;
    this.ratingChange.emit(this.rating);
  }
}