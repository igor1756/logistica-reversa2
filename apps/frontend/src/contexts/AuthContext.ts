import { createContext } from "react";

export type UserData = {
  nome: string;
  matricula: string;
  tipo: string;
};

export type AuthContextType = {
  isAuthenticated: boolean;
  user: UserData | null;
  login: (token: string, userData: UserData) => void;
  logout: () => void;
};

export const AuthContext = createContext<AuthContextType | undefined>(undefined);