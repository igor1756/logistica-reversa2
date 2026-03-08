import { useState } from "react";
import type { ReactNode } from "react";
import { AuthContext} from "./AuthContext";
import type { UserData } from "./AuthContext";

type AuthProviderProps = {
  children: ReactNode;
};

function getStoredUser(): UserData | null {
  const token = localStorage.getItem("token");
  const nome = localStorage.getItem("nome");
  const matricula = localStorage.getItem("matricula");
  const tipo = localStorage.getItem("tipo");

  if (token && nome && matricula && tipo) {
    return { nome, matricula, tipo };
  }

  return null;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<UserData | null>(() => getStoredUser());

  function login(token: string, userData: UserData) {
    localStorage.setItem("token", token);
    localStorage.setItem("nome", userData.nome);
    localStorage.setItem("matricula", userData.matricula);
    localStorage.setItem("tipo", userData.tipo);

    setUser(userData);
  }

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("nome");
    localStorage.removeItem("matricula");
    localStorage.removeItem("tipo");

    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated: !!user,
        user,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
