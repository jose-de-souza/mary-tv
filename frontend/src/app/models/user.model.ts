export interface User {
  id: number;
  username: string;
  role: string;
}

export interface UserUpsert {
  username: string;
  password?: string;
  role: string;
}