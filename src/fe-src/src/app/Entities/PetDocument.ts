export class PetDocument {
  id: number
  petId: number
  documentType: string
  name: string

  constructor (id: number, petId: number, documentType: string, name: string) {
    this.id = id;
    this.petId = petId;
    this.documentType = documentType;
    this.name = name;
  }
}
