import { Component, AfterViewInit, OnDestroy, Renderer2, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

declare global {
  interface Window {
    tns?: any;
  }
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements AfterViewInit, OnDestroy {
  private heroSlider: any;
  private brandSlider: any;

  constructor(
    private renderer: Renderer2,
    @Inject(PLATFORM_ID) private platformId: any
  ) {}

  ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadSliderScripts();
    }
  }

  ngOnDestroy() {
   
  }

  private loadSliderScripts() {
    if ((window as any).tinySliderLoaded) {
      this.initSlider();
      return;
    }

    this.addScript('assets/js/tiny-slider.js', () => {
      (window as any).tinySliderLoaded = true;
      this.initSlider();
    });
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
        onLoad();
      }
    }
  }

  private initSlider() {
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
      }, 300);
    } else {
      console.error('Tiny Slider (tns) is not loaded!');
    }
  }
}
