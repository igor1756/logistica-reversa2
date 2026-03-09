import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { listarHistoricoPorEquipamento, type HistoricoStatus } from "../services/historicos";

export default function HistoricoEquipamento() {
  const { equipamentoId } = useParams();
  const [historico, setHistorico] = useState<HistoricoStatus[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");

  useEffect(() => {
    async function carregarHistorico() {
      try {
        setCarregando(true);
        setErro("");

        if (!equipamentoId) {
          setErro("Equipamento não informado.");
          return;
        }

        const dados = await listarHistoricoPorEquipamento(equipamentoId);
        setHistorico(dados);
      } catch (error) {
        console.error(error);
        setErro("Não foi possível carregar o histórico do equipamento.");
      } finally {
        setCarregando(false);
      }
    }

    carregarHistorico();
  }, [equipamentoId]);

  function formatarData(data: string) {
    return new Date(data).toLocaleString("pt-BR");
  }

  function formatarStatus(status: string) {
    return status
      .replaceAll("_", " ")
      .toLowerCase()
      .replace(/\b\w/g, (letra) => letra.toUpperCase());
  }

  return (
    <div className="mx-auto max-w-5xl p-6">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Histórico do Equipamento</h1>
          <p className="text-sm text-gray-600">
            Linha do tempo das mudanças de status do equipamento.
          </p>
        </div>

        <Link
          to="/equipamentos"
          className="rounded-lg bg-gray-700 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800"
        >
          Voltar
        </Link>
      </div>

      {carregando && (
        <div className="rounded-lg border bg-white p-4 shadow-sm">
          Carregando histórico...
        </div>
      )}

      {!carregando && erro && (
        <div className="rounded-lg border border-red-200 bg-red-50 p-4 text-red-700">
          {erro}
        </div>
      )}

      {!carregando && !erro && historico.length === 0 && (
        <div className="rounded-lg border bg-white p-4 shadow-sm">
          Nenhum histórico encontrado para este equipamento.
        </div>
      )}

      {!carregando && !erro && historico.length > 0 && (
        <div className="space-y-4">
          {historico.map((item) => (
            <div
              key={item.id}
              className="rounded-xl border bg-white p-5 shadow-sm"
            >
              <div className="mb-3 flex flex-col gap-2 md:flex-row md:items-center md:justify-between">
                <div className="text-sm text-gray-500">
                  {formatarData(item.dataMudanca)}
                </div>

                <div className="text-sm font-medium text-gray-700">
                  {formatarStatus(item.statusDe)} → {formatarStatus(item.statusPara)}
                </div>
              </div>

              <div className="mb-2 text-sm text-gray-700">
                <span className="font-semibold">Usuário responsável:</span>{" "}
                {item.nomeUsuario ?? "Sistema"}
              </div>

              <div className="text-sm text-gray-700">
                <span className="font-semibold">Descrição:</span> {item.descricao}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}