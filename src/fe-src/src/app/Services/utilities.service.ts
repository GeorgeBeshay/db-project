import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class UtilitiesService {

  constructor() { }

  async sweetAlertSuccess(msgTitle: string) {
    await Swal.fire({
      position: "center",
      icon: "success",
      title: msgTitle,
      showConfirmButton: false,
      timer: 2000
    });
  }

  async sweetAlertFailure(msgBody: string) {
    await Swal.fire({
      icon: "error",
      title: "Oops...",
      text: msgBody,
    });
  }

  async sweetAlertWarning(title: string, text: string) {
    await Swal.fire({
      title: title,
      text: text,
      icon: "warning"
    });
  }

}
