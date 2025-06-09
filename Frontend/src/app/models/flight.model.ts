export interface Flight {
    id?: number;
    flightNumber: string;
    departureCity: string;
    destinationCity: string;
    departureTime: string;
    arrivalTime: string;
    totalSeats: number;
    price: number;
    airline?: string;
    status?: 'SCHEDULED' | 'DELAYED' | 'CANCELLED' | 'ON_TIME';
}