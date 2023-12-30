export class ApplicationNotification {
    applicationId: number
    adopterId: number
    status: boolean
    date: string

    constructor(
        applicationId?: number,
        adopterId?: number,
        status?: boolean,
        date?: string
      ) {
        this.applicationId = applicationId || -1;
        this.adopterId = adopterId || -1;
        this.status = status || false;
        this.date = date || '';
      }

}