import { Hotel } from "./hotel.model";

export interface HotelAvailability {
    id?: number;
    date: string;
    capacity: number;
    hotel: Hotel;
    userId: number,
    email: string,
    hotelId: number,
    bookingDate: Date;
    quantity: number
  }