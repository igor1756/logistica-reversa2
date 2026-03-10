````markdown
````
# ♻️ Logística Reversa de Equipamentos Corporativos

Sistema web para gestão do ciclo de vida de equipamentos de informática em ambiente corporativo, com foco em **recolhimento**, **avaliação técnica**, **destinação final** e **rastreabilidade do processo**.

---

## 📑 Sumário

- [📌 Sobre o projeto](#-sobre-o-projeto)
- [🚨 Problema que o projeto busca resolver](#-problema-que-o-projeto-busca-resolver)
- [✅ Solução proposta](#-solução-proposta)
- [🧩 Funcionalidades implementadas](#-funcionalidades-implementadas)
- [📐 Regras de negócio implementadas](#-regras-de-negócio-implementadas)
- [🔄 Status do equipamento](#-status-do-equipamento)
- [🏗️ Arquitetura do projeto](#️-arquitetura-do-projeto)
- [🛠️ Stack tecnológica](#️-stack-tecnológica)
- [📂 Estrutura principal do backend](#-estrutura-principal-do-backend)
- [💻 Estrutura principal do frontend](#-estrutura-principal-do-frontend)
- [⚙️ Configuração do ambiente](#️-configuração-do-ambiente)
- [▶️ Como executar o projeto](#️-como-executar-o-projeto)
- [🧪 Fluxo básico para testar o sistema](#-fluxo-básico-para-testar-o-sistema)
- [🌟 Pontos de destaque do MVP](#-pontos-de-destaque-do-mvp)
- [🚧 Limitações atuais / melhorias futuras](#-limitações-atuais--melhorias-futuras)
- [📘 Aprendizados do projeto](#-aprendizados-do-projeto)
- [🏦 Contexto de uso](#-contexto-de-uso)
- [👤 Autor](#-autor)
- [📄 Licença](#-licença)

---

## 📌 Sobre o projeto

Este projeto foi desenvolvido como um **MVP** para demonstrar uma solução de apoio à **logística reversa de ativos de TI** dentro de uma organização.

A proposta é estruturar o fluxo de equipamentos corporativos desde o uso pelo colaborador até sua destinação final, permitindo:

- acompanhar equipamentos vinculados a usuários
- solicitar recolhimento
- registrar avaliação técnica
- aplicar regras automáticas de destinação
- manter histórico de mudanças de status
- apoiar decisões com potencial impacto operacional, financeiro e ambiental

---

## 🚨 Problema que o projeto busca resolver

Em grandes organizações, como bancos e empresas com parque tecnológico amplo, o processo de recolhimento e destinação de equipamentos pode ocorrer de forma descentralizada, pouco rastreável e excessivamente manual.

Isso pode gerar problemas como:

- falta de visibilidade sobre o status dos equipamentos
- baixa rastreabilidade do ciclo de descarte
- perda de valor de ativos ainda reaproveitáveis
- descarte ineficiente
- dificuldade de controle gerencial
- menor apoio a práticas sustentáveis

---

## ✅ Solução proposta

O sistema organiza o fluxo de logística reversa em etapas, permitindo que o processo seja acompanhado de forma estruturada.

### Fluxo principal

1. O equipamento é cadastrado no sistema  
2. Um recolhimento pode ser solicitado para o equipamento  
3. O equipamento passa por avaliação técnica  
4. O sistema calcula automaticamente a destinação final  
5. A mudança de status é registrada em histórico  
6. O processo pode ser consultado visualmente no frontend  

---

## 🧩 Funcionalidades implementadas

### Autenticação
- login com JWT
- rotas protegidas
- persistência de sessão no frontend
- logout funcional

### Gestão de equipamentos
- cadastro de equipamento
- listagem de equipamentos
- atualização de equipamento
- vínculo opcional com usuário responsável

### Solicitação de recolhimento
- criação de solicitação
- listagem de solicitações
- cancelamento de solicitação pendente
- atualização do status do equipamento

### Avaliação técnica
- registro de avaliação técnica
- vínculo com técnico autenticado
- listagem de avaliações por equipamento
- suporte a múltiplas avaliações

### Motor de destinação
- cálculo de idade do equipamento
- cálculo de validade
- cálculo de valor residual com depreciação linear
- decisão automática da destinação final

### Histórico de status
- registro das transições de status
- consulta do histórico por equipamento
- tela de histórico no frontend

---

## 📐 Regras de negócio implementadas

### Depreciação
- depreciação linear de 20% ao ano
- após 5 anos, o valor residual é considerado zero

### Validade
- equipamento com menos de 5 anos completos é considerado válido
- equipamento com 5 anos ou mais é considerado vencido

### Lógica de destinação
- funcionando + válido → venda interna
- funcionando + vencido → doação
- não funciona + válido + recuperável → suporte técnico
- irrecuperável → reciclagem
- não funciona + vencido → reciclagem

---

## 🔄 Status do equipamento

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

## 🏗️ Arquitetura do projeto

O projeto está organizado em formato de **monorepo**, com separação entre backend e frontend.

```text
logistica-reversa2
├─ apps
│  ├─ backend
│  └─ frontend

```



### Backend

Arquitetura em camadas:

```text
Controller
↓
Service
↓
Repository
↓
Database
```

### Frontend

Organizado em páginas, serviços, contexto de autenticação e rotas protegidas.

---

## 🛠️ Stack tecnológica

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

---

## 📂 Estrutura principal do backend

```text
com.logistica.backend
├─ config
├─ controller
├─ domain
├─ repository
├─ security
└─ service
```

### Entidades principais

* `Usuario`
* `Equipamento`
* `SolicitacaoRecolhimento`
* `AvaliacaoTecnica`
* `HistoricoStatus`

---

## 💻 Estrutura principal do frontend

```text
src
├─ contexts
├─ pages
├─ routes
├─ services
├─ App.tsx
├─ main.tsx
└─ index.css
```

### Páginas implementadas

* `Login`
* `Dashboard`
* `EquipamentosList`
* `EquipamentoForm`
* `RecolhimentosList`
* `AvaliacaoForm`
* `AvaliacoesList`
* `HistoricoEquipamento`

---

## ⚙️ Configuração do ambiente

### Backend

Arquivo principal de configuração:

```text
apps/backend/src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/logistica_reversa
spring.datasource.username=logistica
spring.datasource.password=logistica123

spring.jpa.hibernate.ddl-auto=update
spring.flyway.enabled=false

server.port=8080

app.jwt.secret=seu-segredo-aqui
app.jwt.expiration-seconds=28800
```

### Usuário administrador inicial

Criado automaticamente por `AdminInitializer`.

Credenciais padrão:

* matrícula: `0000001`
* senha: `admin123`

---

## ▶️ Como executar o projeto

### Pré-requisitos

* Java 21
* Node.js
* npm
* PostgreSQL
* Git

---

### 1. Clonar o repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd logistica-reversa2
```

---

### 2. Criar o banco de dados

```sql
CREATE DATABASE logistica_reversa;
```

Garanta que o usuário e senha configurados no `application.properties` existam no PostgreSQL.

---

### 3. Executar o backend

```bash
cd apps/backend
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

Backend disponível em:

```text
http://localhost:8080
```

---

### 4. Executar o frontend

```bash
cd apps/frontend
npm install
npm run dev
```

Frontend disponível em:

```text
http://localhost:5173
```

---

## 🧪 Fluxo básico para testar o sistema

1. Faça login com o usuário administrador
2. Cadastre um equipamento
3. Solicite recolhimento do equipamento
4. Registre uma avaliação técnica
5. Verifique a destinação automática do equipamento
6. Consulte o histórico de mudanças de status
7. Consulte a lista de recolhimentos

---

## 🌟 Pontos de destaque do MVP

* fluxo principal funcional de ponta a ponta
* autenticação com JWT
* lógica de negócio de destinação automatizada
* histórico de status auditável
* separação entre frontend e backend
* base preparada para evolução gerencial

---

## 🚧 Limitações atuais / melhorias futuras

O projeto foi construído como MVP e ainda possui espaço para evolução.

### Melhorias futuras previstas

* dashboard gerencial com indicadores agregados
* filtros avançados por status e perfil
* separação mais clara entre visão de usuário e visão administrativa
* publicação completa via web
* melhorias de segurança e autorização
* testes automatizados
* adoção de Flyway para migrações
* indicadores financeiros e ambientais
* integração com sistemas corporativos

---

## 📘 Aprendizados do projeto

Durante a construção do MVP, os principais aprendizados envolveram:

* modelagem incremental de domínio
* alinhamento entre regra de negócio, banco e frontend
* importância da rastreabilidade no processo
* necessidade de validar o fluxo real do usuário
* valor de evoluir o sistema em pequenas etapas funcionais

---

## 🏦 Contexto de uso

Este projeto foi desenvolvido como solução demonstrativa para um cenário de inovação corporativa, com foco em:

* governança de ativos
* reaproveitamento de equipamentos
* sustentabilidade
* rastreabilidade
* apoio à decisão

---

## 👤 Autor

**Igor Rodrigues**

---

## 📄 Licença

MIT License

---
````

```
