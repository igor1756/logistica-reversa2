import { Link } from 'react-router-dom'
import { useAuth } from '../contexts/useAuth'

export default function Dashboard() {
  const { logout } = useAuth()

  return (
    <div className="min-h-screen p-6">
      <h1 className="text-3xl font-bold">Dashboard</h1>

      <div className="mt-6 flex flex-col gap-3">
        <Link
          to="/equipamentos"
          className="rounded border px-4 py-2 hover:bg-gray-100"
        >
          Ver equipamentos
        </Link>

        <Link
          to="/equipamentos/novo"
          className="rounded border px-4 py-2 hover:bg-gray-100"
        >
          Cadastrar equipamento
        </Link>

        <button
          onClick={logout}
          className="w-fit rounded border px-4 py-2 hover:bg-gray-100"
        >
          Sair
        </button>
      </div>
    </div>
  )
}