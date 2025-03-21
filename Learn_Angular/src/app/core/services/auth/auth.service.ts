import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { TokenStorageService } from './token-storage.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = '/Account';

  constructor(private apiService: ApiService, private tokenStorage: TokenStorageService) {}

  // ✅ Gửi request login
  login(payload: { email: string; password: string }): Observable<{ accessToken: string; refreshToken: string }> {
    // var repo = this.apiService.postTypeRequest<{ accessToken: string; refreshToken: string }>(`${this.baseUrl}/Login`, payload);
    return this.apiService.postTypeRequest<{ accessToken: string; refreshToken: string }>(`${this.baseUrl}/Login`, payload).pipe(
      tap(({ accessToken, refreshToken }) => {
        this.tokenStorage.setAccessToken(accessToken);
        this.tokenStorage.setRefreshToken(refreshToken);
      })
    );
  }

  // ✅ Refresh token
  refreshAccessToken(): Observable<{ accessToken: string }> {
    return this.apiService.postTypeRequest<{ accessToken: string }>(`${this.baseUrl}/refresh-token`, { 
      refreshToken: this.tokenStorage.getRefreshToken() 
    }).pipe(
      tap(({ accessToken }) => {
        this.tokenStorage.setAccessToken(accessToken);
      })
    );
  }

  // ✅ Logout (Xóa token)
  logout(): void {
    this.tokenStorage.clearTokens();
  }
}
