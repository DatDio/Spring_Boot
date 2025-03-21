import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root', // Cung cấp service toàn cục
})
export class NotificationToastService {
  constructor(private toastr: ToastrService) {}

  showSuccess(message: string) {
    this.toastr.success(message, 'Thông báo');
  }

  showError(message: string) {
    this.toastr.error(message, 'Lỗi');
  }

  showWarning(message: string) {
    this.toastr.warning(message, 'Cảnh báo');
  }

  showInfo(message: string) {
    this.toastr.info(message, 'Thông tin');
  }
}
