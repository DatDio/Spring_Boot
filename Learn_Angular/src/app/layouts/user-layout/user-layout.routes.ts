import { Routes } from '@angular/router';
import { HomeComponent } from "src/app/user/home/home.component";
import { LoginComponent } from 'src/app/user/login/login.component';
import { ProductListComponent } from 'src/app/user/product-list/product-list.component';
import { UserLayoutComponent } from 'src/app/layouts/user-layout/user-layout.component';
import { CheckoutComponent } from 'src/app/user/checkout/checkout.component';
export const USER_LAYOUT_ROUTES: Routes = [
    {
      path: '',
      component: UserLayoutComponent, // Layout chính chứa Header/Footer
      children: [
        { path: '', component: HomeComponent, pathMatch: 'full' }, // Trang chủ
        { path: 'login', component: LoginComponent },
        { path: 'products', component: ProductListComponent },
        { path: 'checkout', component: CheckoutComponent }
      ]
    }
  ];
  