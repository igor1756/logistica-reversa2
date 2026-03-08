import { Link } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { listarEquipamentos, type EquipamentoResponse } from '../services/equipamentos'

export default function EquipamentosList() {
  const [equipamentos, setEquipamentos] = useState<EquipamentoResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {
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

    carregarEquipamentos()
  }, [])

  return (
    <div className="min-h-screen p-6">
      <div className="mb-6 flex items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold">Equipamentos</h1>
          <p className="mt-1 text-sm text-gray-600">
            Lista de equipamentos cadastrados no sistema.
          </p>
        </div>

        <Link
          to="/equipamentos/novo"
          className="rounded border px-4 py-2 hover:bg-gray-100"
        >
          Novo equipamento
        </Link>
      </div>

      {loading && (
        <p className="text-sm text-gray-600">Carregando equipamentos...</p>
      )}

      {!loading && erro && (
        <div className="rounded border border-red-300 bg-red-50 p-4 text-red-700">
          {erro}
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
              </tr>
            </thead>
            <tbody>
              {equipamentos.map((equipamento) => (
                <tr key={equipamento.id} className="border-b last:border-b-0">
                  <td className="px-4 py-3">{equipamento.numUniversal}</td>
                  <td className="px-4 py-3">{equipamento.modelo}</td>
                  <td className="px-4 py-3">
                    {equipamento.descricao || '-'}
                  </td>
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
                  <td className="px-4 py-3">{equipamento.statusAtual}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}