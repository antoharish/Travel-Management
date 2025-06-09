export interface Itinerary {
  itinerary_id: number;
  travelPackage: TravelPackage;
  totalPrice: number;
  customizedActivities: Activity[];
}

export interface TravelPackage {
  packageId: number;
  name: string;
  noOfDays: number;
  includedHotels: Hotel[];
  includedFlights: Flight[];
  activities: Activity[];
}

export interface Hotel {
  hotelId: number;
  name: string;
  location: string;
  pricePerNight: number;
}

export interface Flight {
  id: number;
  departureCity: string;
  destinationCity: string;
  price: number;
}

export interface Activity {
  activityId: number;
  name: string;
  description: string;
  price: number;
}