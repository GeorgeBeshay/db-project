export class Admin {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  passwordHash: string;

  constructor(
    id?: number,
    firstName?: string,
    lastName?: string,
    email?: string,
    phone?: string,
    passwordHash?: string
  ) {
    this.id = id || -1;
    this.firstName = firstName || '';
    this.lastName = lastName || '';
    this.email = email || '';
    this.phone = phone || '';
    this.passwordHash = passwordHash || '';
  }

  public equals(other: Admin): boolean {
    return (
      this.id === other.id &&
      this.email === other.email &&
      this.passwordHash === other.passwordHash &&
      this.firstName === other.firstName &&
      this.lastName === other.lastName &&
      this.phone === other.phone
    );
  }

  public isValid(): boolean {
    return (
      this.id >= 1 &&
      this.firstName.trim().length > 0 &&
      this.lastName.trim().length > 0 &&
      this.email.trim().length > 0 &&
      this.phone.trim().length > 0 &&
      this.passwordHash.trim().length > 0
    );
  }

}
