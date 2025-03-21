import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations'; // Bắt buộc cho ngx-toastr
import { provideToastr } from 'ngx-toastr';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withFetch()), // ✅ Thêm dòng này để cung cấp HttpClient
    provideClientHydration(withEventReplay()),
    provideAnimations(), // ✅ Cần thiết cho ngx-toastr hoạt động
    provideToastr({
      positionClass: 'toast-top-right', // Hiển thị thông báo ở góc phải dưới
      timeOut: 3000,
      closeButton: true, 
      progressBar:true, // Thời gian hiển thị (3 giây)
      preventDuplicates: true, // Tránh trùng lặp thông báo
    }),
  ],
};
