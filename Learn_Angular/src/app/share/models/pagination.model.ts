export interface PaginationResponse<T> {
    data: T[];
    hasNextPage: boolean;
    totalPages: number;
    totalItems:number;
    currentPage: number;
  }