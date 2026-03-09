import { Link } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { listarEquipamentos, type EquipamentoResponse } from '../services/equipamentos'
import { solicitarRecolhimento } from '../services/recolhimentos'
import axios from 'axios'

function formatarStatus(status: string) {
  switch (status) {
    case 'EM_USO':
      return 'Em uso'
    case 'RECOLHIMENTO_SOLICITADO':
      return 'Recolhimento solicitado'
    case 'RECOLHIDO':
      return 'Recolhido'
    case 'AVALIADO':
      return 'Avaliado'
    case 'DESTINADO_VENDA':
      return 'Destinado à venda'
    case 'DESTINADO_DOACAO':
      return 'Destinado à doação'
    case 'DESTINADO_SUPORTE':
      return 'Destinado ao suporte'
    case 'DESTINADO_RECICLAGEM':
      return 'Destinado à reciclagem'
    default:
      return status
  }
}

export default function EquipamentosList() {
  const [equipamentos, setEquipamentos] = useState<EquipamentoResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')
  const [mensagem, setMensagem] = useState('')
  const [processandoId, setProcessandoId] = useState<string | null>(null)

  async function carregarEquipamentos() {
    try {
      setLoading(true)
      setErro('')
      const data = await listarEquipamentos()
      setEquipamentos(data)
    } catch (error) {
      console.error('Erro ao carregar equipamentos:', error)
      setErro('Não foi possível carregar os equipamentos.')
    } finally {
      setLoading(false)
    }
  }

  async function handleSolicitarRecolhimento(equipamentoId: string) {
    try {
      setErro('')
      setMensagem('')
      setProcessandoId(equipamentoId)

      await solicitarRecolhimento({ equipamentoId })

      setMensagem('Solicitação de recolhimento realizada com sucesso.')
      await carregarEquipamentos()
    } catch (error: unknown) {
      console.error('Erro ao solicitar recolhimento:', error)

      let mensagemErro = 'Não foi possível solicitar o recolhimento.'

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
    void carregarEquipamentos()
  }, [])

  const podeSolicitarRecolhimento = (status: string) => status === 'EM_USO'

  const podeAvaliar = (status: string) =>
    status === 'RECOLHIMENTO_SOLICITADO' || status === 'AVALIADO'

  const podeVerAvaliacoes = (status: string) =>
    status === 'AVALIADO' ||
    status === 'DESTINADO_VENDA' ||
    status === 'DESTINADO_DOACAO' ||
    status === 'DESTINADO_SUPORTE' ||
    status === 'DESTINADO_RECICLAGEM'


  return (
    <div className="min-h-screen p-6">
      <div className="mb-6 flex items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold">Equipamentos</h1>
          <p className="mt-1 text-sm text-gray-600">
            Lista de equipamentos cadastrados no sistema.
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
            to="/equipamentos/novo"
            className="rounded border px-4 py-2 hover:bg-gray-100"
          >
            Novo equipamento
          </Link>
          <Link
            to="/recolhimentos"
            className="rounded border px-4 py-2 hover:bg-gray-100"
          >
            Ver solicitações de recolhimento
          </Link>
        </div>
      </div>

      {loading && (
        <p className="text-sm text-gray-600">Carregando equipamentos...</p>
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

      {!loading && !erro && equipamentos.length === 0 && (
        <div className="rounded border p-4">
          <p className="text-sm text-gray-600">
            Nenhum equipamento cadastrado até o momento.
          </p>
        </div>
      )}

      {!loading && !erro && equipamentos.length > 0 && (
        <div className="overflow-x-auto rounded border">
          <table className="min-w-full border-collapse">
            <thead>
              <tr className="border-b bg-gray-50 text-left">
                <th className="px-4 py-3">Nº Universal</th>
                <th className="px-4 py-3">Modelo</th>
                <th className="px-4 py-3">Descrição</th>
                <th className="px-4 py-3">Data Aquisição</th>
                <th className="px-4 py-3">Valor</th>
                <th className="px-4 py-3">Responsável</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3">Ações</th>
              </tr>
            </thead>
            <tbody>
              {equipamentos.map((equipamento) => (
                <tr key={equipamento.id} className="border-b last:border-b-0">
                  <td className="px-4 py-3">{equipamento.numUniversal}</td>
                  <td className="px-4 py-3">{equipamento.modelo}</td>
                  <td className="px-4 py-3">{equipamento.descricao || '-'}</td>
                  <td className="px-4 py-3">
                    {new Date(equipamento.dataAquisicao).toLocaleDateString('pt-BR')}
                  </td>
                  <td className="px-4 py-3">
                    {Number(equipamento.valorAquisicao).toLocaleString('pt-BR', {
                      style: 'currency',
                      currency: 'BRL',
                    })}
                  </td>
                  <td className="px-4 py-3">
                    {equipamento.nomeUsuario || 'Sem responsável'}
                  </td>
                  <td className="px-4 py-3">
                    {formatarStatus(equipamento.statusAtual)}
                  </td>
                  <td className="px-4 py-3">
                    <div className="flex flex-wrap gap-2">
                      {podeSolicitarRecolhimento(equipamento.statusAtual) && (
                        <button
                          type="button"
                          onClick={() => void handleSolicitarRecolhimento(equipamento.id)}
                          disabled={processandoId === equipamento.id}
                          className="rounded bg-amber-600 px-3 py-2 text-sm text-white hover:bg-amber-700 disabled:cursor-not-allowed disabled:opacity-50"
                        >
                          {processandoId === equipamento.id
                            ? 'Solicitando...'
                            : 'Solicitar recolhimento'}
                        </button>
                      )}

                      {podeAvaliar(equipamento.statusAtual) && (
                        <Link
                          to={`/equipamentos/${equipamento.id}/avaliar`}
                          className="rounded bg-blue-600 px-3 py-2 text-sm text-white hover:bg-blue-700"
                        >
                          Avaliar
                        </Link>
                      )}

                      {podeVerAvaliacoes(equipamento.statusAtual) && (
                        <Link
                          to={`/equipamentos/${equipamento.id}/avaliacoes`}
                          className="rounded border px-3 py-2 text-sm hover:bg-gray-100"
                        >
                          Ver avaliações
                        </Link>
                      )}
                      
                      <Link
                        to={`/equipamentos/${equipamento.id}/historico`}
                        className="rounded-lg bg-slate-600 px-3 py-2 text-sm font-medium text-white hover:bg-slate-700"
                      >
                        Ver histórico
                      </Link>
                    </div>
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