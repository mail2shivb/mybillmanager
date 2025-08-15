import client from './client';
import { mockAPI } from './mockData';

// Track backend availability
let backendAvailable: boolean | null = null;
let lastCheck = 0;
const CHECK_INTERVAL = 30000; // 30 seconds

// Function to check if backend is available
const checkBackendAvailability = async (): Promise<boolean> => {
  const now = Date.now();
  
  // Return cached result if checked recently
  if (backendAvailable !== null && (now - lastCheck) < CHECK_INTERVAL) {
    return backendAvailable;
  }

  try {
    // Try to ping the backend with a simple request
    await client.get('/health', { timeout: 5000 });
    backendAvailable = true;
    lastCheck = now;
    console.log('✅ Backend is available');
    return true;
  } catch (error) {
    // If health endpoint doesn't exist, try the properties endpoint
    try {
      await client.get('/properties', { timeout: 5000 });
      backendAvailable = true;
      lastCheck = now;
      console.log('✅ Backend is available (via /properties)');
      return true;
    } catch (propertiesError) {
      backendAvailable = false;
      lastCheck = now;
      console.log('❌ Backend not available, using mock data');
      return false;
    }
  }
};

// Wrapper function for API calls with automatic fallback
export const apiCall = async <T>(
  backendCall: () => Promise<T>,
  mockCall: () => Promise<T>,
  operationName: string
): Promise<T> => {
  // Force mock data if environment variable is set
  if (import.meta.env.VITE_USE_MOCK_DATA === 'true') {
    console.log(`🎭 Using mock data for ${operationName} (forced by env var)`);
    return await mockCall();
  }

  const isBackendAvailable = await checkBackendAvailability();
  
  if (isBackendAvailable) {
    try {
      const result = await backendCall();
      console.log(`🌐 Successfully used backend for ${operationName}`);
      return result;
    } catch (error) {
      console.warn(`⚠️ Backend call failed for ${operationName}, falling back to mock data:`, error);
      // Mark backend as unavailable and use mock data
      backendAvailable = false;
      return await mockCall();
    }
  } else {
    console.log(`🎭 Using mock data for ${operationName}`);
    return await mockCall();
  }
};

// Reset backend availability check (useful for retry scenarios)
export const resetBackendCheck = () => {
  backendAvailable = null;
  lastCheck = 0;
};
