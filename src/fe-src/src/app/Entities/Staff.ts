export class Staff {
  id: number;
  firstName: string;
  lastName: string;
  role: string;
  phone: string;
  email: string;
  passwordHash: string;
  shelterId: number;

  constructor(
    id?: number,
    firstName?: string,
    lastName?: string,
    role?: string,
    phone?: string,
    email?: string,
    passwordHash?: string,
    shelterId?: number
  ) {
    this.id = id || 0;
    this.firstName = firstName || '';
    this.lastName = lastName || '';
    this.role = role || '';
    this.phone = phone || '';
    this.email = email || '';
    this.passwordHash = passwordHash || '';
    this.shelterId = shelterId || 0;
  }

  toString(): string {
    return `Staff{
      id=${this.id},
      firstName='${this.firstName}',
      lastName='${this.lastName}',
      role='${this.role}',
      phone='${this.phone}',
      email='${this.email}',
      passwordHash='${this.passwordHash}',
      shelterId=${this.shelterId}
    }`;
  }

  equals(other: Staff): boolean {
    return (
      this.id === other.id &&
      this.passwordHash === other.passwordHash &&
      this.shelterId === other.shelterId &&
      this.firstName === other.firstName &&
      this.lastName === other.lastName &&
      this.role === other.role &&
      this.phone === other.phone &&
      this.email === other.email
    );
  }

}
