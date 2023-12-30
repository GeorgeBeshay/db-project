export class Shelter {
    id: number;
    name: string;
    location: string;
    email: string;
    phone: string;
    manager: number;

    constructor(id?:number,name?:string,location?:string,email?:string,phone?:string,manager?:number){
        this.id = id || -1;
        this.name = name || '';
        this.location = location || '';
        this.email = email || '';
        this.phone = phone || '';
        this.manager = manager || -1;
    }
}