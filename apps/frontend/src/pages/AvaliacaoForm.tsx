import { Link, useNavigate, useParams } from 'react-router-dom'
import { useState } from 'react'
import axios from 'axios'
import { criarAvaliacao } from '../services/avaliacoes'

export default function AvaliacaoForm() {
  const { equipamentoId } = useParams()
  const navigate = useNavigate()

  const [estaFuncionando, setEstaFuncionando] = useState('true')
  const [ehRecuperavel, setEhRecuperavel] = useState('true')
  const [descricao, setDescricao] = useState('')
  const [erro, setErro] = useState('')
  const [salvando, setSalvando] = useState(false)

  async function handleSubmit(event: { preventDefault: () => void }) {
    event.preventDefault()

    if (!equipamentoId) {
      setErro('Equipamento não informado.')
      return
    }

    try {
      setErro('')
      setSalvando(true)

      await criarAvaliacao({
        equipamentoId,
        estaFuncionando: estaFuncionando === 'true',
        ehRecuperavel: ehRecuperavel === 'true',
        descricao,
      })

      navigate(`/equipamentos/${equipamentoId}/avaliacoes`)
    } catch (error: unknown) {
      console.error('Erro ao salvar avaliação:', error)

      let mensagemErro = 'Não foi possível registrar a avaliação.'

      if (axios.isAxiosError(error)) {
        const message = error.response?.data?.message

        if (typeof message === 'string' && message.trim() !== '') {
          mensagemErro = message
        }
      }

      setErro(mensagemErro)
    } finally {
      setSalvando(false)
    }
  }

  return (
    <div className="min-h-screen p-6">
      <div className="mx-auto max-w-3xl">
        <div className="mb-6 flex items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold">Avaliação técnica</h1>
            <p className="mt-1 text-sm text-gray-600">
              Registre o resultado da análise técnica do equipamento.
            </p>
          </div>

          <div className="flex gap-2">
            <Link
              to="/equipamentos"
              className="rounded border px-4 py-2 hover:bg-gray-100"
            >
              Voltar
            </Link>
          </div>
        </div>

        <form
          onSubmit={(event) => void handleSubmit(event)}
          className="space-y-6 rounded border bg-white p-6 shadow-sm"
        >
          {erro && (
            <div className="rounded border border-red-300 bg-red-50 p-4 text-red-700">
              {erro}
            </div>
          )}

          <div>
            <label className="mb-2 block text-sm font-medium">
              Equipamento
            </label>
            <input
              type="text"
              value={equipamentoId || ''}
              disabled
              className="w-full rounded border bg-gray-100 px-3 py-2 text-sm text-gray-600"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium">
              Está funcionando?
            </label>
            <select
              value={estaFuncionando}
              onChange={(e) => setEstaFuncionando(e.target.value)}
              className="w-full rounded border px-3 py-2"
            >
              <option value="true">Sim</option>
              <option value="false">Não</option>
            </select>
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium">
              É recuperável?
            </label>
            <select
              value={ehRecuperavel}
              onChange={(e) => setEhRecuperavel(e.target.value)}
              className="w-full rounded border px-3 py-2"
            >
              <option value="true">Sim</option>
              <option value="false">Não</option>
            </select>
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium">
              Descrição
            </label>
            <textarea
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              rows={5}
              className="w-full rounded border px-3 py-2"
              placeholder="Descreva o estado do equipamento, defeitos encontrados e observações técnicas."
            />
          </div>

          <div className="flex gap-3">
            <button
              type="submit"
              disabled={salvando}
              className="rounded bg-blue-600 px-4 py-2 text-white hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50"
            >
              {salvando ? 'Salvando...' : 'Salvar avaliação'}
            </button>

            <Link
              to="/equipamentos"
              className="rounded border px-4 py-2 hover:bg-gray-100"
            >
              Cancelar
            </Link>
          </div>
        </form>
      </div>
    </div>
  )
}