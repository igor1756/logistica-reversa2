import { api } from './api'

export type EquipamentoResponse = {
  id: string
  numUniversal: string
  modelo: string
  descricao: string | null
  dataAquisicao: string
  valorAquisicao: number
  usuarioId: string | null
  nomeUsuario: string | null
  statusAtual: string
}

export type EquipamentoRequest = {
  numUniversal: string
  modelo: string
  descricao: string
  dataAquisicao: string
  valorAquisicao: number
  usuarioId: string | null
}

export async function listarEquipamentos() {
  const response = await api.get<EquipamentoResponse[]>('/equipamentos')
  return response.data
}

export async function criarEquipamento(payload: EquipamentoRequest) {
  const response = await api.post<EquipamentoResponse>('/equipamentos', payload)
  return response.data
}