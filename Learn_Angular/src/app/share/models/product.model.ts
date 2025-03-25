export interface Product {
  id?: number;
  name: string;
  brandId:number;
  categoryId:number;
  description: string;
  imageUrl?: string;
  imageFile?: File;
  productColors: ProductColor[];
}
export interface ProductColor {
  id?: number;
  color: string;
  price: number;
  imageUrl?: string;
  variants: ProductVariant[];
}
export interface ProductVariant {
  id?: number;
  productSize: string;
  stockQuantity: number;
}




