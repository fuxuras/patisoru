import './App.css'
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import {Routes, Route} from 'react-router'

function App() {

  return (
    <Routes>
      <Route path="/" element={<HomePage></HomePage>}></Route>
      <Route path="/login" element={<LoginPage></LoginPage>}></Route>
      <Route path="/register" element={<RegisterPage></RegisterPage>}></Route>
    </Routes>
  )
}

export default App
