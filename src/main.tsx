import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import { BrowserRouter, Route, Routes, Link } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { store } from './store';
import 'bootstrap/dist/css/bootstrap.min.css';
import PropertyList from './pages/property/PropertyList';
import PropertyForm from './pages/property/PropertyForm';

const qc = new QueryClient();
function App() {
  return (
    <>
      <nav className="navbar navbar-dark bg-dark">
        <div className="container">
          <Link className="navbar-brand" to="/">
            MBM
          </Link>
          <ul className="navbar-nav flex-row gap-3">
            <li className="nav-item">
              <Link to="/" className="nav-link">
                Properties
              </Link>
            </li>
          </ul>
        </div>
      </nav>
      <div className="container my-4">
        <Routes>
          <Route path="/" element={<PropertyList />} />
          <Route path="/properties/new" element={<PropertyForm />} />
          <Route path="/properties/:id" element={<PropertyForm />} />
        </Routes>
      </div>
    </>
  );
}
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Provider store={store}>
      <QueryClientProvider client={qc}>
        <BrowserRouter>
          <App />
        </BrowserRouter>
      </QueryClientProvider>
    </Provider>
  </React.StrictMode>
);
