import { ApplicationStatus } from "./ApplicationStatus"

export class AdoptionApplication {
    id: number
    adopterId: number
    petId: number
    status: ApplicationStatus
    description: string
    experience: boolean
    creationDate: string
    closingDate: string

    constructor(id: number, adopterId: number, petId: number, status: ApplicationStatus,
        description: string, experience: boolean, creationDate: string = "", closingDate: string = "") {

        this.id = id;
        this.adopterId = adopterId;
        this.petId = petId;
        this.status = status;
        this.description = description;
        this.experience = experience;
        this.creationDate = creationDate;
        this.closingDate = closingDate;
    }
}