import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/useAuth";

export function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate("/");
  }

  return (
    <div className="min-h-screen bg-slate-100">
      <header className="bg-white shadow-sm border-b border-slate-200">
        <div className="max-w-6xl mx-auto px-4 py-4 flex items-center justify-between">
          <div>
            <h1 className="text-xl font-bold text-slate-800">Dashboard</h1>
            <p className="text-sm text-slate-500">
              Sistema de Logística Reversa
            </p>
          </div>

          <button
            onClick={handleLogout}
            className="bg-slate-800 text-white px-4 py-2 rounded-lg hover:bg-slate-700 transition"
          >
            Sair
          </button>
        </div>
      </header>

      <main className="max-w-6xl mx-auto px-4 py-8">
        <div className="bg-white rounded-2xl shadow p-6">
          <h2 className="text-lg font-semibold text-slate-800 mb-4">
            Bem-vindo ao sistema
          </h2>

          <div className="space-y-2 text-slate-700">
            <p>
              <strong>Nome:</strong> {user?.nome}
            </p>
            <p>
              <strong>Matrícula:</strong> {user?.matricula}
            </p>
            <p>
              <strong>Perfil:</strong> {user?.tipo}
            </p>
          </div>
        </div>
      </main>
    </div>
  );
}
