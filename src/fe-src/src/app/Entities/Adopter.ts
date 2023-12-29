export class Adopter {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    passwordHash: string;
    birthDate: string;
    gender: boolean;
    address: string;

    constructor(
        id?: number,
        firstName?: string,
        lastName?: string,
        email?: string,
        phone?: string,
        passwordHash?: string,
        birthDate?: string,
        gender?: boolean,
        address?: string
      ) {
        this.id = id || -1;
        this.firstName = firstName || '';
        this.lastName = lastName || '';
        this.email = email || '';
        this.phone = phone || '';
        this.passwordHash = passwordHash || '';
        this.birthDate = birthDate || '';
        this.gender = gender || false;
        this.address = address || '';
      }

}