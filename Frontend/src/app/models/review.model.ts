// import { Hotel } from './hotel.model';
// import { User } from './user.model';

// export interface Review {
//   reviewID?: number;
//   hotel: Hotel;
//   user: User;
//   rating: number;
//   comment: string;
//   timestamp?: Date;
// }

export interface Review {
  reviewID?: number;
  hotel: {
      hotelId: number;
      name?: string;
  };
  user: {
      userId: number;
      username?: string;
  };
  rating: number;
  comment: string;
  timestamp?: Date;
}