import { Injectable } from '@angular/core';
@Injectable({
  providedIn: 'root',
})
export class TokenStorageService {
    private ACCESS_TOKEN_KEY = 'accessToken';
    private REFRESH_TOKEN_KEY = 'refreshToken';
  
    constructor() {}
  
    // ✅ Lưu token vào cookies
    private setCookie(name: string, value: string, days: number): void {
      const expires = new Date();
      expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
      document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/;secure`;
    }
  
    // ✅ Lấy token từ cookies
    private getCookie(name: string): string | null {
      const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
      return match ? match[2] : null;
    }
  
    // ✅ Xóa token khỏi cookies
    private deleteCookie(name: string): void {
      document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/;`;
    }
  
    // ✅ Lấy accessToken
    getAccessToken(): string | null {
      return this.getCookie(this.ACCESS_TOKEN_KEY);
    }
  
    // ✅ Lấy refreshToken
    getRefreshToken(): string | null {
      return this.getCookie(this.REFRESH_TOKEN_KEY);
    }
  
    // ✅ Lưu token
    setAccessToken(token: string): void {
      this.setCookie(this.ACCESS_TOKEN_KEY, token, 1); // Lưu trong 1 ngày
    }
  
    setRefreshToken(token: string): void {
      this.setCookie(this.REFRESH_TOKEN_KEY, token, 7); // Lưu trong 7 ngày
    }
  
    // ✅ Xóa token khi logout
    clearTokens(): void {
      this.deleteCookie(this.ACCESS_TOKEN_KEY);
      this.deleteCookie(this.REFRESH_TOKEN_KEY);
    }

}