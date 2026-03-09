import { api } from "./api";

export type HistoricoStatus = {
  id: string;
  equipamentoId: string;
  statusDe: string;
  statusPara: string;
  dataMudanca: string;
  usuarioId: string | null;
  nomeUsuario: string | null;
  descricao: string;
};

export async function listarHistoricoPorEquipamento(equipamentoId: string) {
  const response = await api.get<HistoricoStatus[]>(
    `/historicos/equipamento/${equipamentoId}`
  );

  return response.data;
}