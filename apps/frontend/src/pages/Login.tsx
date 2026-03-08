import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../services/api";
import { useAuth } from "../contexts/useAuth";

export function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [matricula, setMatricula] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setErro("");
    setCarregando(true);

    try {
      const response = await api.post("/auth/login", {
        matricula,
        senha,
      });

      const data = response.data;

      login(data.token, {
        nome: data.nome,
        matricula: data.matricula,
        tipo: data.tipo,
      });

      navigate("/dashboard");
    } catch {
      setErro("Matrícula ou senha inválidas.");
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="min-h-screen bg-slate-100 flex items-center justify-center px-4">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-8">
        <h1 className="text-2xl font-bold text-center text-slate-800 mb-2">
          Logística Reversa
        </h1>

        <p className="text-sm text-slate-500 text-center mb-6">
          Acesse o sistema com sua matrícula e senha
        </p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">
              Matrícula
            </label>
            <input
              type="text"
              value={matricula}
              onChange={(e) => setMatricula(e.target.value)}
              className="w-full border border-slate-300 rounded-lg px-3 py-2 outline-none focus:border-slate-500"
              placeholder="Digite sua matrícula"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">
              Senha
            </label>
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              className="w-full border border-slate-300 rounded-lg px-3 py-2 outline-none focus:border-slate-500"
              placeholder="Digite sua senha"
            />
          </div>

          {erro && (
            <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
              {erro}
            </div>
          )}

          <button
            type="submit"
            disabled={carregando}
            className="w-full bg-slate-800 text-white rounded-lg py-2 font-medium hover:bg-slate-700 transition disabled:opacity-60"
          >
            {carregando ? "Entrando..." : "Entrar"}
          </button>
        </form>
      </div>
    </div>
  );
}
