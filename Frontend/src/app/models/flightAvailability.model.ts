import { Flight } from './flight.model';

export interface FlightAvailability {
    id: number;
    flight: Flight;
    date: string;
    availableSeats: number;
}