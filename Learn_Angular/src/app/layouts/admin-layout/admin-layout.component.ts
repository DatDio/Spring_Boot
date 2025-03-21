import { Component, Renderer2, Inject, PLATFORM_ID, AfterViewInit, OnDestroy,OnInit } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterOutlet, Router } from '@angular/router';
import { HeaderComponent } from 'src/app/admin/header/header.component';
import { SidebarComponent } from 'src/app/admin/sidebar/sidebar.component';
import { filter } from 'rxjs/operators';
import { NavigationEnd } from '@angular/router';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-admin-layout',
  imports: [RouterOutlet, HeaderComponent, SidebarComponent],
  standalone: true,
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent implements OnInit, OnDestroy {
  private cssLinks: HTMLLinkElement[] = [];
  private scripts: HTMLScriptElement[] = [];

  constructor(
    private renderer: Renderer2,
    @Inject(PLATFORM_ID) private platformId: any
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadAdminStyles();
      this.loadAdminScripts();
    }
  }

  ngOnDestroy() {
    if (isPlatformBrowser(this.platformId)) {
      this.removeAdminStyles();
      this.removeAdminScripts();
    }
  }

  private loadAdminStyles() {
    const styles = [
      'assets/admin/assets/vendors/feather/feather.css',
      'assets/admin/assets/vendors/mdi/css/materialdesignicons.min.css',
      'assets/admin/assets/vendors/ti-icons/css/themify-icons.css',
      'assets/admin/assets/vendors/font-awesome/css/font-awesome.min.css',
      'assets/admin/assets/vendors/typicons/typicons.css',
      'assets/admin/assets/vendors/simple-line-icons/css/simple-line-icons.css',
      'assets/admin/assets/vendors/css/vendor.bundle.base.css',
      'assets/admin/assets/vendors/bootstrap-datepicker/bootstrap-datepicker.min.css',
      'assets/admin/assets/vendors/datatables.net-bs4/dataTables.bootstrap4.css',
      'assets/admin/assets/js/select.dataTables.min.css',
      'assets/admin/assets/css/style.css'
    ];
    styles.forEach(href => this.addCss(href));
  }

  private loadAdminScripts() {
    const scripts = [
      'assets/admin/assets/vendors/js/vendor.bundle.base.js',
      'assets/admin/assets/vendors/bootstrap-datepicker/bootstrap-datepicker.min.js',
      'assets/admin/assets/vendors/chart.js/chart.umd.js',
      'assets/admin/assets/vendors/progressbar.js/progressbar.min.js',
      'assets/admin/assets/js/off-canvas.js',
      'assets/admin/assets/js/template.js',
      'assets/admin/assets/js/settings.js',
      'assets/admin/assets/js/hoverable-collapse.js',
      'assets/admin/assets/js/todolist.js',
      'assets/admin/assets/js/jquery.cookie.js',
      'assets/admin/assets/js/dashboard.js'
    ];
    scripts.forEach(src => this.addScript(src));
  }

  private addCss(href: string) {
    const link = this.renderer.createElement('link');
    link.rel = 'stylesheet';
    link.href = href;
    link.classList.add('admin-style'); // Đánh dấu để dễ xóa
    this.renderer.appendChild(document.head, link);
    this.cssLinks.push(link);
  }

  private addScript(src: string) {
    const script = this.renderer.createElement('script');
    script.src = src;
    script.async = false;
    script.defer = false;
    script.classList.add('admin-script'); // Đánh dấu để dễ xóa
    this.renderer.appendChild(document.body, script);
    this.scripts.push(script);
  }

  private removeAdminStyles() {
    this.cssLinks.forEach(link => this.renderer.removeChild(document.head, link));
    this.cssLinks = [];
  }

  private removeAdminScripts() {
    this.scripts.forEach(script => this.renderer.removeChild(document.body, script));
    this.scripts = [];
  }
}

