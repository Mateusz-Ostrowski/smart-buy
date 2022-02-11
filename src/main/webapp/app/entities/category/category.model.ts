import { IProduct } from 'app/entities/product/product.model';

export interface ICategory {
  id?: number;
  name?: string | null;
  order?: number | null;
  parentCategory?: ICategory | null;
  products?: IProduct[] | null;
  subcategories?: ICategory[] | null;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string | null,
    public order?: number | null,
    public parentCategory?: ICategory | null,
    public products?: IProduct[] | null,
    public subcategories?: ICategory[] | null
  ) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
