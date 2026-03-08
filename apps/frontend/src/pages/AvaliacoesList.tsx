import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import {
  listarAvaliacoesPorEquipamento,
  type AvaliacaoTecnicaResponse,
} from "../services/avaliacoes";

export function AvaliacoesList() {
  const { equipamentoId } = useParams();
  const [avaliacoes, setAvaliacoes] = useState<AvaliacaoTecnicaResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState("");

  useEffect(() => {
    async function carregarAvaliacoes() {
      if (!equipamentoId) {
        setErro("Equipamento não informado.");
        setLoading(false);
        return;
      }

      try {
        const dados = await listarAvaliacoesPorEquipamento(equipamentoId);
        setAvaliacoes(dados);
      } catch {
        setErro("Não foi possível carregar as avaliações.");
      } finally {
        setLoading(false);
      }
    }

    carregarAvaliacoes();
  }, [equipamentoId]);

  return (
    <div className="mx-auto max-w-5xl p-6">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Avaliações Técnicas</h1>
          <p className="text-sm text-gray-600">
            Histórico de avaliações do equipamento selecionado.
          </p>
        </div>

        <div className="flex gap-2">
          {equipamentoId && (
            <Link
              to={`/equipamentos/${equipamentoId}/avaliar`}
              className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
            >
              Nova avaliação
            </Link>
          )}

          <Link
            to="/equipamentos"
            className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium hover:bg-gray-50"
          >
            Voltar
          </Link>
        </div>
      </div>

      {loading && (
        <div className="rounded-lg border bg-white p-4 shadow-sm">
          Carregando avaliações...
        </div>
      )}

      {!loading && erro && (
        <div className="rounded-lg border border-red-200 bg-red-50 p-4 text-red-700">
          {erro}
        </div>
      )}

      {!loading && !erro && avaliacoes.length === 0 && (
        <div className="rounded-lg border bg-white p-4 shadow-sm">
          Nenhuma avaliação encontrada para este equipamento.
        </div>
      )}

      {!loading && !erro && avaliacoes.length > 0 && (
        <div className="space-y-4">
          {avaliacoes.map((avaliacao) => (
            <div
              key={avaliacao.id}
              className="rounded-xl border bg-white p-5 shadow-sm"
            >
              <div className="mb-3 flex flex-wrap items-center justify-between gap-2">
                <div>
                  <h2 className="text-lg font-semibold">
                    {avaliacao.modelo} - {avaliacao.numeroPatrimonio}
                  </h2>
                  <p className="text-sm text-gray-600">
                    Técnico: {avaliacao.nomeTecnico}
                  </p>
                </div>

                <span className="text-sm text-gray-500">
                  {new Date(avaliacao.dataAvaliacao).toLocaleString("pt-BR")}
                </span>
              </div>

              <div className="mb-3 grid gap-3 md:grid-cols-2">
                <div className="rounded-lg bg-gray-50 p-3">
                  <p className="text-xs font-semibold uppercase text-gray-500">
                    Está funcionando
                  </p>
                  <p className="mt-1 font-medium">
                    {avaliacao.estaFuncionando ? "Sim" : "Não"}
                  </p>
                </div>

                <div className="rounded-lg bg-gray-50 p-3">
                  <p className="text-xs font-semibold uppercase text-gray-500">
                    É recuperável
                  </p>
                  <p className="mt-1 font-medium">
                    {avaliacao.ehRecuperavel ? "Sim" : "Não"}
                  </p>
                </div>
              </div>

              <div className="rounded-lg bg-gray-50 p-3">
                <p className="text-xs font-semibold uppercase text-gray-500">
                  Descrição
                </p>
                <p className="mt-1 text-sm text-gray-700">
                  {avaliacao.descricao || "Sem descrição informada."}
                </p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}