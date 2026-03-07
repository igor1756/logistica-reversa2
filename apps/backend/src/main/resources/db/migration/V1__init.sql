CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    matricula VARCHAR(7) UNIQUE,
    nome VARCHAR(100),
    tipo VARCHAR(30),
    setor VARCHAR(100),
    senha_hash VARCHAR(255)
);
