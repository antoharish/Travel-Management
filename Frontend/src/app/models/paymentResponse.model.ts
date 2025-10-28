export interface PaymentResponse {
    sessionId: string;
    status: string;
    amount: number;
    currency: string;
    paymentType: string;
    sessionUrl: string;
}