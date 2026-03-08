import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { Login } from './pages/Login'
import Dashboard from './pages/Dashboard'
import EquipamentosList from './pages/EquipamentosList'
import EquipamentoForm from './pages/EquipamentoForm'
import ProtectedRoute from './routes/ProtectedRoute'

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

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}