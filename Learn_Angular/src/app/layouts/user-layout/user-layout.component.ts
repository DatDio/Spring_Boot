import { Component, Renderer2, AfterViewInit, OnDestroy, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from 'src/app/share/footer/footer.component';
import { HeaderComponent } from "src/app/user/header/header.component";
import { HomeComponent } from "src/app/user/home/home.component";

declare global {
  interface Window {
    tns?: any;
  }
}

@Component({
  selector: 'app-user-layout',
  standalone: true,
  imports: [RouterOutlet, FooterComponent, HeaderComponent],
  templateUrl: './user-layout.component.html',
  styleUrls: ['./user-layout.component.css']
})
export class UserLayoutComponent implements AfterViewInit, OnDestroy {
  private heroSlider: any;
  private brandSlider: any;

  constructor(
    private renderer: Renderer2,
    @Inject(PLATFORM_ID) private platformId: any
  ) {}

  ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadStyles();
      this.loadScripts();
    }
  }

  ngOnDestroy() {
    if (this.heroSlider) {
      this.heroSlider.destroy();
      this.heroSlider = null;
    }
    if (this.brandSlider) {
      this.brandSlider.destroy();
      this.brandSlider = null;
    }
  }

  private loadStyles() {
    const styles = [
      'assets/css/bootstrap.min.css',
      'assets/css/LineIcons.3.0.css',
      'assets/css/tiny-slider.css',
      'assets/css/glightbox.min.css',
      'assets/css/main.css'
    ];
    styles.forEach(href => this.addCss(href));
  }

  private loadScripts() {
    if ((window as any).tinySliderLoaded) {
      this.runCustomScript(); // Nếu script đã load, chỉ cần chạy lại slider
      return;
    }

    const scripts = [
      'assets/js/bootstrap.min.js',
      'assets/js/tiny-slider.js',
      'assets/js/glightbox.min.js',
      'assets/js/main.js'
    ];
    let loadedCount = 0;

    scripts.forEach(src => {
      this.addScript(src, () => {
        loadedCount++;
        if (loadedCount === scripts.length) {
          (window as any).tinySliderLoaded = true; // Đánh dấu đã load xong
          this.runCustomScript();
        }
      });
    });
  }

  private addCss(href: string) {
    if (isPlatformBrowser(this.platformId)) {
      const existingLink = document.querySelector(`link[href="${href}"]`);
      if (!existingLink) {
        const link = this.renderer.createElement('link');
        link.rel = 'stylesheet';
        link.href = href;
        this.renderer.appendChild(document.head, link);
      }
    }
  }

  private addScript(src: string, onLoad?: () => void) {
    if (isPlatformBrowser(this.platformId)) {
      const existingScript = document.querySelector(`script[src="${src}"]`);
      if (!existingScript) {
        const script = this.renderer.createElement('script');
        script.src = src;
        script.async = false;
        script.defer = false;
        if (onLoad) {
          script.onload = onLoad;
        }
        this.renderer.appendChild(document.body, script);
      } else if (onLoad) {
        onLoad(); // Nếu script đã có sẵn, gọi luôn callback
      }
    }
  }

  private runCustomScript() {
    if (isPlatformBrowser(this.platformId) && window.tns) {
      setTimeout(() => {
        this.heroSlider = window.tns({
          container: '.hero-slider',
          slideBy: 'page',
          autoplay: true,
          autoplayButtonOutput: false,
          mouseDrag: true,
          gutter: 0,
          items: 1,
          nav: false,
          controls: true,
          controlsText: ['<i class="lni lni-chevron-left"></i>', '<i class="lni lni-chevron-right"></i>']
        });

        this.brandSlider = window.tns({
          container: '.brands-logo-carousel',
          autoplay: true,
          autoplayButtonOutput: false,
          mouseDrag: true,
          gutter: 15,
          nav: false,
          controls: false,
          responsive: {
            0: { items: 1 },
            540: { items: 3 },
            768: { items: 5 },
            992: { items: 6 }
          }
        });
      }, 300); // Delay để chắc chắn `tiny-slider` đã được load
    } else {
      console.error('Tiny Slider (tns) is not loaded!');
    }
  }
}
