import axios from 'axios'
const client = axios.create({ baseURL: import.meta.env.VITE_API_BASE || 'http://localhost:8080/api', headers:{'Content-Type':'application/json'} })
export default client
