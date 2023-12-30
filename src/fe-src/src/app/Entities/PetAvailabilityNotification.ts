export class PetAvailabilityNotification {
    petId: number
    adopterId: number
    status: boolean
    date: string

    constructor(
        petId?: number,
        adopterId?: number,
        status?: boolean,
        date?: string
      ) {
        this.petId = petId || -1;
        this.adopterId = adopterId || -1;
        this.status = status || false;
        this.date = date || '';
      }

}