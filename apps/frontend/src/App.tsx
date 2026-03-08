import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { Login } from './pages/Login'
import Dashboard from './pages/Dashboard'
import EquipamentosList from './pages/EquipamentosList'
import EquipamentoForm from './pages/EquipamentoForm'
import ProtectedRoute from './routes/ProtectedRoute'
import RecolhimentosList from './pages/RecolhimentosList'
import { AvaliacoesList } from './pages/AvaliacoesList'
import AvaliacaoForm from './pages/AvaliacaoForm'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route
          path="/"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/equipamentos"
          element={
            <ProtectedRoute>
              <EquipamentosList />
            </ProtectedRoute>
          }
        />

        <Route
          path="/equipamentos/novo"
          element={
            <ProtectedRoute>
              <EquipamentoForm />
            </ProtectedRoute>
          }
        />
        <Route
          path="/recolhimentos"
          element={
            <ProtectedRoute>
              <RecolhimentosList />
            </ProtectedRoute>
          }
        />
        <Route
          path="/equipamentos/:equipamentoId/avaliacoes"
          element={
            <ProtectedRoute>
              <AvaliacoesList />
            </ProtectedRoute>
          }
        />
        <Route
          path="/equipamentos/:equipamentoId/avaliar"
          element={
            <ProtectedRoute>
              <AvaliacaoForm />
            </ProtectedRoute>
          }
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}