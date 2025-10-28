// export interface Hotel {
//     hotelId: number;
//     name?: string;
//     location?: string;
//     roomsAvailable?: number;
//     rating?: number;
//     pricePerNight?: number;

import { Review } from "./review.model";

//   }
export interface Hotel {
  id: number;
  hotelId: number;
  name: string;
  location: string;
  description?: string;
  roomsAvailable?: number;
  rating?: number;
  pricePerNight: number;
  reviews?: Review[];
  amenities?: string[];
  images?: string[];
}
