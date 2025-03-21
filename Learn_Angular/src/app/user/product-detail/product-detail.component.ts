import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from 'src/app/core/services/product/product.service';
import { Product } from 'src/app/share/models/product.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit {
  product: Product | null = null; // Lưu thông tin sản phẩm
  isLoading = true; // Trạng thái loading
  errorMessage: string | null = null; // Lưu lỗi nếu có
  constructor(private productService: ProductService,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    console.log("Đã vào ngOnInit");
    this.loadProduct();
  }

  // loadProduct() {
  //   var productId = this.route.snapshot.paramMap.get('id'); // Lấy ID từ URL
  //   productId = '6'; // Gán tạm để kiểm thử

  //   if (productId) {
  //     this.isLoading = true;
  //     this.productService.getProductById(+productId).subscribe({
  //       next: (response) => {
  //         console.log('🔥 Response gốc từ API:', response); // Log toàn bộ response

  //         this.product = response; // Nếu muốn xử lý tiếp
  //         this.isLoading = false;
  //       },
  //       error: (error) => {
  //         console.error('❌ Lỗi từ API:', error); // Log lỗi chi tiết
  //         this.errorMessage = error.message || 'Lỗi khi tải dữ liệu!';
  //         this.isLoading = false;
  //       },
  //     });
  //   } else {
  //     this.errorMessage = 'ID sản phẩm không hợp lệ!';
  //     console.error('❌ ID sản phẩm không hợp lệ');
  //     this.isLoading = false;
  //   }
  // }



  loadProduct() {
    var productId = this.route.snapshot.paramMap.get('id'); // Lấy ID từ URL
    productId = '6';
    if (productId) {
      this.isLoading = true;
      this.productService.getProductById(+productId).subscribe({
        next: (product) => {
          this.product = product; // Dữ liệu đã được xử lý trước đó
          console.log(this.product);
          this.isLoading = false;
        },
        error: (error) => {
          this.errorMessage = error.message || 'Lỗi khi tải dữ liệu!';
          this.isLoading = false;
          console.error(error.message);
        },
      });
    } else {
      this.errorMessage = 'ID sản phẩm không hợp lệ!';
      console.error('ID sản phẩm không hợp lệ');
      this.isLoading = false;
    }
  }

}
