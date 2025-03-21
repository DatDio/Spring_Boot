import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { TokenStorageService } from './token-storage.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private isRefreshing = false;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    const accessToken = this.tokenStorage.getAccessToken();

    // ✅ Nếu có token, thêm vào request
    if (accessToken) {
      authReq = this.addToken(req, accessToken);
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        // ❌ Nếu lỗi 401 (Token hết hạn), thử refresh token
        if (error.status === 401 && !this.isRefreshing) {
          return this.handle401Error(req, next);
        }
        return throwError(() => new Error(error.message));
      })
    );
  }

  private addToken(req: HttpRequest<any>, token: string) {
    return req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  private handle401Error(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.isRefreshing = true;
    return this.authService.refreshAccessToken().pipe(
      switchMap(({ accessToken }) => {
        this.isRefreshing = false;
        return next.handle(this.addToken(req, accessToken));
      }),
      catchError((error) => {
        this.isRefreshing = false;
        this.authService.logout(); // ❌ Nếu refresh thất bại, logout user
        return throwError(() => new Error(error.message));
      })
    );
  }
}
