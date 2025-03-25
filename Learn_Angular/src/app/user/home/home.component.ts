import { Component, AfterViewInit, OnDestroy, Renderer2, Inject, PLATFORM_ID, OnInit } from '@angular/core';
import { isPlatformBrowser,CommonModule  } from '@angular/common';
import { ProductService } from 'src/app/core/services/product/product.service';
import { Product } from '@models/product.model';
import { RouterModule } from '@angular/router';
import { environment } from '@environments/environment';
declare global {
  interface Window {
    tns?: any;
  }
}

@Component({
  standalone: true,
  selector: 'app-home',
  imports:[RouterModule,CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, AfterViewInit, OnDestroy {
  imageBaseUrl = environment.imageBaseUrl;
  private heroSlider: any;
  private brandSlider: any;
  isLoading: boolean = true;
  products: Product[] = [];
  currentPage: number = 1;
  pageSize: number = 12;
  totalPages: number = 1;
  pagesToShow: number[] = [];
  totalItems: number = 0;
  searchParams = {
    name: '',
    brandId: null as number | null,
    categoryId: null as number | null,
    priceFrom: null as number | null,
    priceTo: null as number | null,
    createdFrom: null as string | null,
    createdTo: null as string | null,
    sortBy: 'createAt',
    direction: 'desc'
  };
  constructor(
    private renderer: Renderer2,
    @Inject(PLATFORM_ID) private platformId: any,
    private productService: ProductService,
  ) { }

  ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadSliderScripts();
    }
  }
  ngOnInit(): void {
    this.loadProducts();
  }
  ngOnDestroy() {

  }
  /** Gọi API lấy danh sách sản phẩm */
  loadProducts(): void {


    const params = {
      ...this.searchParams,
      page: this.currentPage,
      size: this.pageSize
    };

    this.productService.searchProducts(params).subscribe({
      next: (data) => {
        this.products = data.data;
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        this.isLoading = false;
        //this.updatePagination();
      },
      error: () => {
        //this.errorMessage = 'Lỗi khi tải danh sách sản phẩm!';
        this.isLoading = false;
      }
    });
  }
  getPriceRange(product: Product): string {
    const { minPrice, maxPrice } = this.productService.getMinMaxPrice(product);
    return minPrice === maxPrice ? `${minPrice} VND` : `${minPrice} - ${maxPrice} VND`;
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
