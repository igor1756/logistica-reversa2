import { Link } from 'react-router-dom'
import { useEffect, useState } from 'react'
import axios from 'axios'
import {
  cancelarRecolhimento,
  listarRecolhimentos,
  type SolicitacaoRecolhimentoResponse,
} from '../services/recolhimentos'

function formatarStatus(status: string) {
  switch (status) {
    case 'PENDENTE':
      return 'Pendente'
    case 'CANCELADA':
      return 'Cancelada'
    default:
      return status
  }
}

function formatarData(data: string) {
  return new Date(data).toLocaleString('pt-BR')
}

export default function RecolhimentosList() {
  const [recolhimentos, setRecolhimentos] = useState<SolicitacaoRecolhimentoResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')
  const [mensagem, setMensagem] = useState('')
  const [processandoId, setProcessandoId] = useState<string | null>(null)

  async function carregarRecolhimentos() {
    try {
      setLoading(true)
      setErro('')
      const data = await listarRecolhimentos()
      setRecolhimentos(data)
    } catch (error) {
      console.error('Erro ao carregar recolhimentos:', error)
      setErro('Não foi possível carregar as solicitações de recolhimento.')
    } finally {
      setLoading(false)
    }
  }

  async function handleCancelar(id: string) {
    try {
      setErro('')
      setMensagem('')
      setProcessandoId(id)

      await cancelarRecolhimento(id)

      setMensagem('Solicitação cancelada com sucesso.')
      await carregarRecolhimentos()
    } catch (error: unknown) {
      console.error('Erro ao cancelar recolhimento:', error)

      let mensagemErro = 'Não foi possível cancelar a solicitação.'

      if (axios.isAxiosError(error)) {
        const message = error.response?.data?.message

        if (typeof message === 'string' && message.trim() !== '') {
          mensagemErro = message
        }
      }

      setErro(mensagemErro)
    } finally {
      setProcessandoId(null)
    }
  }

  useEffect(() => {
    void carregarRecolhimentos()
  }, [])

  return (
    <div className="min-h-screen p-6">
      <div className="mb-6 flex items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold">Solicitações de recolhimento</h1>
          <p className="mt-1 text-sm text-gray-600">
            Acompanhe as solicitações abertas no sistema.
          </p>
        </div>

        <div className="flex gap-2">
          <Link
            to="/"
            className="rounded border px-4 py-2 hover:bg-gray-100"
          >
            Voltar ao dashboard
          </Link>

          <Link
            to="/equipamentos"
            className="rounded border px-4 py-2 hover:bg-gray-100"
          >
            Ver equipamentos
          </Link>
        </div>
      </div>

      {loading && (
        <p className="text-sm text-gray-600">
          Carregando solicitações de recolhimento...
        </p>
      )}

      {!loading && erro && (
        <div className="rounded border border-red-300 bg-red-50 p-4 text-red-700">
          {erro}
        </div>
      )}

      {!loading && mensagem && (
        <div className="rounded border border-green-300 bg-green-50 p-4 text-green-700">
          {mensagem}
        </div>
      )}

      {!loading && !erro && recolhimentos.length === 0 && (
        <div className="rounded border p-4">
          <p className="text-sm text-gray-600">
            Nenhuma solicitação de recolhimento encontrada.
          </p>
        </div>
      )}

      {!loading && !erro && recolhimentos.length > 0 && (
        <div className="overflow-x-auto rounded border">
          <table className="min-w-full border-collapse">
            <thead>
              <tr className="border-b bg-gray-50 text-left">
                <th className="px-4 py-3">Equipamento</th>
                <th className="px-4 py-3">Solicitante</th>
                <th className="px-4 py-3">Data da solicitação</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3">Ações</th>
              </tr>
            </thead>
            <tbody>
              {recolhimentos.map((recolhimento) => (
                <tr key={recolhimento.id} className="border-b last:border-b-0">
                  <td className="px-4 py-3">{recolhimento.nomeEquipamento}</td>
                  <td className="px-4 py-3">{recolhimento.nomeUsuarioSolicitante}</td>
                  <td className="px-4 py-3">
                    {formatarData(recolhimento.dataSolicitacao)}
                  </td>
                  <td className="px-4 py-3">
                    {formatarStatus(recolhimento.status)}
                  </td>
                  <td className="px-4 py-3">
                    {recolhimento.status === 'PENDENTE' ? (
                      <button
                        type="button"
                        onClick={() => void handleCancelar(recolhimento.id)}
                        disabled={processandoId === recolhimento.id}
                        className="rounded bg-red-600 px-3 py-2 text-sm text-white hover:bg-red-700 disabled:cursor-not-allowed disabled:opacity-50"
                      >
                        {processandoId === recolhimento.id
                          ? 'Cancelando...'
                          : 'Cancelar'}
                      </button>
                    ) : (
                      <span className="text-sm text-gray-400">-</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}