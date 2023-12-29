export class Pet
{
  id: number
  name: string
  specie: string
  breed: string
  birthdate: string
  gender: boolean
  healthStatus: string
  behaviour: string
  description: string
  shelterId: number
  neutering: boolean
  houseTraining: boolean
  vaccination: boolean

  constructor(id: number, name: string = "", specie: string = "", breed: string = "", birthdate: string = "",
    gender: boolean = true, healthStatus: string = "", behaviour: string = "", description: string = "",
    shelterId: number, neutering: boolean = true, houseTraining: boolean = true, vaccination: boolean = true) {

    this.id = id;
    this.name = name;
    this.specie = specie;
    this.breed = breed;
    this.birthdate = birthdate;
    this.gender = gender;
    this.healthStatus = healthStatus;
    this.behaviour = behaviour;
    this.description = description;
    this.shelterId = shelterId;
    this.neutering = neutering;
    this.houseTraining = houseTraining;
    this.vaccination = vaccination;
  }
}
