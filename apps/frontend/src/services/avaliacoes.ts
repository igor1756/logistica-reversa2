import { api } from "./api";

// eh enviado para o backend quando o tecnico criar uma nova avaliacao tecnica para um equipamento
export type AvaliacaoTecnicaRequest = {
  equipamentoId: string;
  estaFuncionando: boolean;
  ehRecuperavel: boolean;
  descricao: string;
};

// eh recebido do backend quando o frontend solicitar as avaliacoes tecnicas de um equipamento
export type AvaliacaoTecnicaResponse = {
  id: string;
  equipamentoId: string;
  numeroPatrimonio: string;
  modelo: string;
  descricaoEquipamento: string;
  tecnicoId: string;
  nomeTecnico: string;
  dataAvaliacao: string;
  estaFuncionando: boolean;
  ehRecuperavel: boolean;
  descricao: string;
};

export async function criarAvaliacao(payload: AvaliacaoTecnicaRequest) {
  const response = await api.post<AvaliacaoTecnicaResponse>("/avaliacoes", payload);
  return response.data;
}

export async function listarAvaliacoesPorEquipamento(equipamentoId: string) {
  const response = await api.get<AvaliacaoTecnicaResponse[]>(
    `/avaliacoes/equipamento/${equipamentoId}`
  );
  return response.data;
}