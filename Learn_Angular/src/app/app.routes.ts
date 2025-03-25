import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { UserLayoutComponent } from './layouts/user-layout/user-layout.component';
import { NotFoundComponent } from './share/not-found/not-found.component';
export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./layouts/user-layout/user-layout.routes').then(m => m.USER_LAYOUT_ROUTES)
  },
  {
    path: 'admin',
    component: AdminLayoutComponent,
    loadChildren: () => import('./layouts/admin-layout/admin-layout.routes').then(m => m.ADMIN_LAYOUT_ROUTES)
  },
  {
    path: '**', redirectTo: '404NotFound'
  },
  { path: '404NotFound', component: NotFoundComponent }

];
