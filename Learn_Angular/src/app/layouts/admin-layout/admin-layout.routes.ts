import { Routes } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { AdminhomeComponent } from 'src/app/admin/adminhome/adminhome.component';
import { ProductDashboardComponent } from 'src/app/admin/product/product-dashboard/product-dashboard.component';
import { AddProductComponent } from 'src/app/admin/product/add-product/add-product.component';
import { EditProductComponent } from 'src/app/admin/product/edit-product/edit-product.component';
export const ADMIN_LAYOUT_ROUTES: Routes = [
   { path: '', component: AdminhomeComponent },
   { path: 'products', component: ProductDashboardComponent },
   { path: 'add-product', component: AddProductComponent },
   { path: 'edit-product/:id', component: EditProductComponent }, 
];
