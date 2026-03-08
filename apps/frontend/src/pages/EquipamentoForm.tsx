import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { criarEquipamento } from '../services/equipamentos'

export default function EquipamentoForm() {
  const navigate = useNavigate()

  const [numUniversal, setNumUniversal] = useState('')
  const [modelo, setModelo] = useState('')
  const [descricao, setDescricao] = useState('')
  const [dataAquisicao, setDataAquisicao] = useState('')
  const [valorAquisicao, setValorAquisicao] = useState('')
  const [usuarioId, setUsuarioId] = useState('')

  const [loading, setLoading] = useState(false)
  const [erro, setErro] = useState('')

  async function salvarEquipamento() {
    try {
      setLoading(true)
      setErro('')

      await criarEquipamento({
        numUniversal,
        modelo,
        descricao,
        dataAquisicao,
        valorAquisicao: Number(valorAquisicao),
        usuarioId: usuarioId.trim() ? usuarioId.trim() : null,
      })

      navigate('/equipamentos')
    } catch (error) {
      console.error('Erro ao cadastrar equipamento:', error)
      setErro('Não foi possível cadastrar o equipamento.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen p-6">
      <div className="mx-auto max-w-2xl">
        <h1 className="text-2xl font-bold">Novo Equipamento</h1>
        <p className="mt-1 text-sm text-gray-600">
          Preencha os dados para cadastrar um novo equipamento.
        </p>

        <form
          onSubmit={(event) => {
            event.preventDefault()
            void salvarEquipamento()
          }}
          className="mt-6 space-y-4 rounded border p-6"
        >
          <div>
            <label htmlFor="numUniversal" className="mb-1 block text-sm font-medium">
              Número universal
            </label>
            <input
              id="numUniversal"
              type="text"
              value={numUniversal}
              onChange={(e) => setNumUniversal(e.target.value)}
              className="w-full rounded border px-3 py-2"
              required
            />
          </div>

          <div>
            <label htmlFor="modelo" className="mb-1 block text-sm font-medium">
              Modelo
            </label>
            <input
              id="modelo"
              type="text"
              value={modelo}
              onChange={(e) => setModelo(e.target.value)}
              className="w-full rounded border px-3 py-2"
              required
            />
          </div>

          <div>
            <label htmlFor="descricao" className="mb-1 block text-sm font-medium">
              Descrição
            </label>
            <textarea
              id="descricao"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              className="w-full rounded border px-3 py-2"
              rows={4}
            />
          </div>

          <div>
            <label htmlFor="dataAquisicao" className="mb-1 block text-sm font-medium">
              Data de aquisição
            </label>
            <input
              id="dataAquisicao"
              type="date"
              value={dataAquisicao}
              onChange={(e) => setDataAquisicao(e.target.value)}
              className="w-full rounded border px-3 py-2"
              required
            />
          </div>

          <div>
            <label htmlFor="valorAquisicao" className="mb-1 block text-sm font-medium">
              Valor de aquisição
            </label>
            <input
              id="valorAquisicao"
              type="number"
              step="0.01"
              min="0"
              value={valorAquisicao}
              onChange={(e) => setValorAquisicao(e.target.value)}
              className="w-full rounded border px-3 py-2"
              required
            />
          </div>

          <div>
            <label htmlFor="usuarioId" className="mb-1 block text-sm font-medium">
              ID do usuário responsável
            </label>
            <input
              id="usuarioId"
              type="text"
              value={usuarioId}
              onChange={(e) => setUsuarioId(e.target.value)}
              className="w-full rounded border px-3 py-2"
              placeholder="Opcional"
            />
            <p className="mt-1 text-xs text-gray-500">
              Deixe em branco para cadastrar sem responsável.
            </p>
          </div>

          {erro && (
            <div className="rounded border border-red-300 bg-red-50 p-3 text-sm text-red-700">
              {erro}
            </div>
          )}

          <div className="flex gap-3">
            <button
              type="submit"
              disabled={loading}
              className="rounded border px-4 py-2 hover:bg-gray-100 disabled:opacity-60"
            >
              {loading ? 'Salvando...' : 'Salvar'}
            </button>

            <button
              type="button"
              onClick={() => navigate('/equipamentos')}
              className="rounded border px-4 py-2 hover:bg-gray-100"
            >
              Cancelar e listar equipamentos
            </button>
            <button
              type="button"
              onClick={() => navigate('/')}
              className="rounded border px-4 py-2 hover:bg-gray-100"
            >
              Dashboard
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}