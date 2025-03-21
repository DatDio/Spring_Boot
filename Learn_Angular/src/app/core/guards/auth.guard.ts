import { CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const isAuthenticated = !!localStorage.getItem('token'); // Kiểm tra token

  return isAuthenticated; // Nếu chưa đăng nhập -> return false (chặn truy cập)
};
