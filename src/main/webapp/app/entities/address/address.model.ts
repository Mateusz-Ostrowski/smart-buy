import { ICustomer } from 'app/entities/customer/customer.model';

export interface IAddress {
  id?: number;
  city?: string | null;
  postalCode?: string | null;
  street?: string | null;
  buildingNo?: string | null;
  doorNo?: string | null;
  customer?: ICustomer | null;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public city?: string | null,
    public postalCode?: string | null,
    public street?: string | null,
    public buildingNo?: string | null,
    public doorNo?: string | null,
    public customer?: ICustomer | null
  ) {}
}

export function getAddressIdentifier(address: IAddress): number | undefined {
  return address.id;
}
