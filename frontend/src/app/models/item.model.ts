export interface Item {
  id: number;
  title: string;
  description: string;
  iconUrl: string;
  videoUrl: string;
  itemDate: Date;
  isNew: boolean;
  isHeadline: boolean;
  contentType: string;
  parentId: number;
  categoryId: number;
  eventId: number;
  children: Item[];
}

export interface ItemUpsert {
  title: string;
  description: string;
  iconUrl: string;
  videoUrl: string;
  itemDate: Date;
  isNew: boolean;
  isHeadline: boolean;
  contentType: string;
  parentId: number;
  categoryId: number;
  eventId: number;
}