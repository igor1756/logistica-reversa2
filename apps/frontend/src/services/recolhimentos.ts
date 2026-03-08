export type StatusSolicitacaoRecolhimento = "PENDENTE" | "CANCELADA";

export interface SolicitacaoRecolhimentoRequest {
    equipamentoId: string;
}

export interface SolicitacaoRecolhimentoResponse {
    id: string;
    equipamentoId: string;
    nomeEquipamento: string;
    usuarioSolicitanteId: string;
    nomeUsuarioSolicitante: string;
    dataSolicitacao: string;
    status: StatusSolicitacaoRecolhimento;
}

import { api } from "./api";

export async function solicitarRecolhimento(
    payload: SolicitacaoRecolhimentoRequest
) {
    const response = await api.post<SolicitacaoRecolhimentoResponse>( // SolicitacaoRecolhimentoResponse é o tipo de resposta que esperamos receber do backend
        "/recolhimentos",
        payload
    );

    return response.data;
}

export async function listarRecolhimentos() {
    const response = await api.get<SolicitacaoRecolhimentoResponse[]>(
        "/recolhimentos"
    );

    return response.data;
}

export async function cancelarRecolhimento(id: string) {
    const response = await api.patch<SolicitacaoRecolhimentoResponse>(
        `/recolhimentos/${id}/cancelar`
    );

    return response.data;
}