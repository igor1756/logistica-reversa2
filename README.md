
# Logística Reversa de Equipamentos Corporativos

Sistema web para gestão do ciclo de vida de equipamentos de informática em ambiente corporativo, com foco em **recolhimento**, **avaliação técnica**, **destinação final** e **rastreabilidade do processo**.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Problema que o projeto busca resolver](#problema-que-o-projeto-busca-resolver)
- [Solução proposta](#solução-proposta)
- [Fluxo principal](#fluxo-principal)
- [Funcionalidades implementadas](#funcionalidades-implementadas)
- [Regras de negócio implementadas](#regras-de-negócio-implementadas)
- [Status do equipamento](#status-do-equipamento)
- [Arquitetura do projeto](#arquitetura-do-projeto)
- [Stack tecnológica](#stack-tecnológica)
- [Arquitetura do backend](#arquitetura-do-backend)
- [Autenticação](#autenticação)
- [Domínio implementado](#domínio-implementado)
- [Estrutura de diretórios](#estrutura-de-diretórios)

---

## Visão Geral

O projeto foi desenvolvido como um **MVP** para demonstrar uma solução de apoio à **logística reversa de ativos físicos de TI** dentro de uma organização.

A proposta é estruturar o fluxo de equipamentos corporativos desde o uso pelo colaborador até sua destinação final, permitindo:

- acompanhar equipamentos vinculados a usuários
- solicitar recolhimento
- registrar avaliação técnica
- aplicar regras automáticas de destinação
- manter histórico de mudanças de status
- apoiar decisões com potencial impacto operacional, financeiro e ambiental

---

## Problema que o projeto busca resolver

Em grandes organizações, como bancos e empresas com parque tecnológico amplo, o processo de recolhimento e destinação de equipamentos pode ocorrer de forma descentralizada, pouco rastreável e excessivamente manual.

Isso pode gerar problemas como:

- falta de visibilidade sobre o status dos equipamentos
- baixa rastreabilidade do ciclo de descarte
- perda de valor de ativos ainda reaproveitáveis
- descarte ineficiente
- dificuldade de controle gerencial
- menor apoio a práticas sustentáveis

---

## Solução proposta

O sistema organiza o fluxo de logística reversa em etapas, permitindo que o processo seja acompanhado de forma estruturada, com regras de negócio bem definidas e atualização automática do estado do equipamento.

### Fluxo principal

1. O equipamento é cadastrado no sistema
2. Um recolhimento pode ser solicitado para o equipamento
3. O equipamento passa por avaliação técnica
4. O sistema calcula automaticamente a destinação final
5. A mudança de status é registrada em histórico
6. O processo pode ser consultado visualmente no frontend

---

## Funcionalidades implementadas

### 1. Autenticação
- login com JWT
- rotas protegidas
- persistência de sessão no frontend
- logout funcional

### 2. Gestão de equipamentos
- cadastro de equipamento
- listagem de equipamentos
- atualização de equipamento
- vínculo opcional com usuário responsável

### 3. Solicitação de recolhimento
- criação de solicitação
- listagem de solicitações
- cancelamento de solicitação pendente
- atualização do status do equipamento

### 4. Avaliação técnica
- registro de avaliação técnica
- vínculo com técnico autenticado
- listagem de avaliações por equipamento
- suporte a múltiplas avaliações

### 5. Motor de destinação
- cálculo de idade do equipamento
- cálculo de validade
- cálculo de valor residual com depreciação linear
- decisão automática da destinação final

### 6. Histórico de status
- registro das transições de status
- consulta do histórico por equipamento
- tela de histórico no frontend

---

## Regras de negócio implementadas

### RN01 — Depreciação
- depreciação linear de 20% ao ano
- após 5 anos, o valor residual é considerado zero

### RN02 — Validade
- equipamento com menos de 5 anos completos é considerado válido
- equipamento com 5 anos ou mais é considerado vencido

### RN03 — Lógica de destinação
- funcionando + válido → venda interna
- funcionando + vencido → doação
- não funciona + válido + recuperável → suporte técnico
- irrecuperável → reciclagem
- não funciona + vencido → reciclagem

### RN04 — Cancelamento
- o usuário só pode cancelar solicitação enquanto o status for `PENDENTE`

### RN05 — Solicitação única pendente
- só pode existir uma solicitação pendente por equipamento

### RN06 — Fonte oficial do estado
- o sistema usa `statusAtual` em `Equipamento` como fonte oficial do estado do equipamento

---

## Status do equipamento

O sistema trabalha com os seguintes status:

- `EM_USO`
- `RECOLHIMENTO_SOLICITADO`
- `RECOLHIDO`
- `AVALIADO`
- `DESTINADO_VENDA`
- `DESTINADO_DOACAO`
- `DESTINADO_SUPORTE`
- `DESTINADO_RECICLAGEM`

---

## Arquitetura do projeto

O projeto está organizado em formato de **monorepo**, com separação entre backend e frontend.

```text
logistica-reversa2
├─ apps
│  ├─ backend
│  └─ frontend
````

---

## Stack tecnológica

### Backend

* Java 21
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Lombok
* PostgreSQL

### Frontend

* React 19
* TypeScript
* Vite 7
* TailwindCSS 4
* Axios
* React Router 7

### Banco de dados

* PostgreSQL
* database: `logistica_reversa`

### Ambiente

* Ubuntu 24.04

---

## Arquitetura do backend

O backend segue uma arquitetura em camadas:

```text
Controller
↓
Service
↓
Repository
↓
Database
```

---

## Autenticação

O sistema já possui autenticação com:

* JWT
* Spring Security
* login funcional
* contexto autenticado no frontend
* rotas protegidas

### Fluxo de autenticação

```text
Frontend → /auth/login → Backend → JWT → token salvo no frontend → acesso protegido
```

---

## Domínio implementado

### Entidades principais

* `Usuario`
* `Equipamento`
* `SolicitacaoRecolhimento`
* `AvaliacaoTecnica`
* `HistoricoStatus`

### Relacionamentos principais

* `Usuario` 1:N `Equipamento`
* `Equipamento` 1:N `SolicitacaoRecolhimento`
* `Equipamento` 1:N `AvaliacaoTecnica`
* `Equipamento` 1:N `HistoricoStatus`

### Observações importantes do domínio

* o vínculo de `Equipamento` com `Usuario` é opcional
* a regra de “apenas uma solicitação pendente por equipamento” é tratada na camada de serviço
* o sistema utiliza `statusAtual` em `Equipamento` como fonte oficial do estado do equipamento

---

## Estrutura de diretórios

### Backend

```text
com.logistica.backend
├── BackendApplication.java
├── config
│   ├── AdminInitializer.java
│   └── SecurityConfig.java
├── controller
│   ├── auth
│   │   ├── AuthController.java
│   │   └── dto
│   ├── avaliacao
│   │   ├── AvaliacaoTecnicaController.java
│   │   └── dto
│   ├── equipamento
│   │   ├── dto
│   │   └── EquipamentoController.java
│   ├── historico
│   │   ├── dto
│   │   └── HistoricoStatusController.java
│   ├── recolhimento
│   │   ├── dto
│   │   └── SolicitacaoRecolhimentoController.java
│   └── TestController.java
├── domain
│   ├── avaliacao
│   │   └── AvaliacaoTecnica.java
│   ├── equipamento
│   │   ├── Equipamento.java
│   │   └── StatusEquipamento.java
│   ├── historico
│   │   └── HistoricoStatus.java
│   ├── recolhimento
│   │   ├── SolicitacaoRecolhimento.java
│   │   └── StatusSolicitacaoRecolhimento.java
│   └── usuario
│       ├── UserRole.java
│       └── Usuario.java
├── repository
│   ├── AvaliacaoTecnicaRepository.java
│   ├── EquipamentoRepository.java
│   ├── HistoricoStatusRepository.java
│   ├── SolicitacaoRecolhimentoRepository.java
│   └── UsuarioRepository.java
├── security
│   ├── AuthenticatedUser.java
│   └── JwtAuthenticationFilter.java
└── service
    ├── auth
    │   ├── AuthService.java
    │   └── JwtService.java
    ├── avaliacao
    │   └── AvaliacaoTecnicaService.java
    ├── destinacao
    │   └── DestinacaoService.java
    ├── equipamento
    │   └── EquipamentoService.java
    ├── historico
    │   └── HistoricoStatusService.java
    ├── recolhimento
    │   └── SolicitacaoRecolhimentoService.java
    └── usuario
        └── CustomUserDetailsService.java

```

### Frontend

```text
frontend
├── dist
│   ├── assets
│   ├── index.html
│   └── vite.svg
├── eslint.config.js
├── index.html
├── node_modules
│   ├── ...
├── package.json
├── package-lock.json
├── public
│   └── vite.svg
├── README.md
├── src
│   ├── api
│   ├── App.css
│   ├── App.tsx
│   ├── assets
│   │   └── react.svg
│   ├── components
│   ├── contexts
│   │   ├── AuthContext.ts
│   │   ├── AuthProvider.tsx
│   │   └── useAuth.ts
│   ├── index.css
│   ├── main.tsx
│   ├── pages
│   │   ├── AvaliacaoForm.tsx
│   │   ├── AvaliacoesList.tsx
│   │   ├── Dashboard.tsx
│   │   ├── EquipamentoForm.tsx
│   │   ├── EquipamentosList.tsx
│   │   ├── HistoricoEquipamento.tsx
│   │   ├── Login.tsx
│   │   └── RecolhimentosList.tsx
│   ├── routes
│   │   └── ProtectedRoute.tsx
│   └── services
│       ├── api.ts
│       ├── avaliacoes.ts
│       ├── equipamentos.ts
│       ├── historicos.ts
│       └── recolhimentos.ts
├── tsconfig.app.json
├── tsconfig.json
├── tsconfig.node.json
└── vite.config.ts
```

---

## Estado atual do projeto

Atualmente, o sistema já contempla:

* autenticação funcional
* gestão de equipamentos
* fluxo de solicitação de recolhimento
* avaliação técnica
* motor automático de destinação
* histórico de mudanças de status
* frontend com telas para operação e consulta

Trata-se de um MVP funcional com foco em demonstrar a viabilidade da solução, sua estrutura técnica e seu potencial de aplicação em ambiente corporativo.

---

## Objetivo do MVP

Demonstrar, de forma prática, como um sistema pode apoiar a logística reversa de equipamentos corporativos, aumentando:

* a rastreabilidade do processo
* a eficiência operacional
* a governança sobre ativos
* o reaproveitamento de equipamentos
* a aderência a práticas sustentáveis

---

## Observação

Este projeto foi construído com foco em demonstração funcional, validação da ideia e evolução incremental. Novas funcionalidades podem ser adicionadas futuramente, como:

* dashboard gerencial com indicadores
* filtros e relatórios avançados
* trilha de auditoria mais completa
* notificações
* perfis de acesso mais detalhados
* integração com sistemas corporativos

---
