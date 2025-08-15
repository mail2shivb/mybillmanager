import { useState, useEffect } from 'react';
import { resetBackendCheck } from '../api/apiHandler';

export default function ApiStatus() {
  const [lastApiCall, setLastApiCall] = useState<string>('');
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    // Listen for console logs to detect API mode
    const originalLog = console.log;
    console.log = (...args) => {
      originalLog(...args);
      const message = args.join(' ');
      if (message.includes('🌐 Successfully used backend') || 
          message.includes('🎭 Using mock data') ||
          message.includes('⚠️ Backend call failed')) {
        setLastApiCall(message);
        setIsVisible(true);
        // Hide after 5 seconds
        setTimeout(() => setIsVisible(false), 5000);
      }
    };

    return () => {
      console.log = originalLog;
    };
  }, []);

  const handleRetryBackend = () => {
    resetBackendCheck();
    setLastApiCall('');
    setIsVisible(false);
  };

  if (!isVisible || !lastApiCall) return null;

  const isUsingBackend = lastApiCall.includes('🌐 Successfully used backend');
  const isUsingMock = lastApiCall.includes('🎭 Using mock data');
  const hasFailed = lastApiCall.includes('⚠️ Backend call failed');

  return (
    <div className={`alert alert-dismissible fade show ${
      isUsingBackend ? 'alert-success' : 
      hasFailed ? 'alert-warning' : 
      'alert-info'
    }`} role="alert">
      <div className="d-flex justify-content-between align-items-center">
        <div>
          <small>
            {isUsingBackend && '🌐 Using Backend API'}
            {isUsingMock && '🎭 Using Demo Data'}
            {hasFailed && '⚠️ Backend unavailable, using demo data'}
          </small>
        </div>
        <div>
          {!isUsingBackend && (
            <button 
              className="btn btn-sm btn-outline-primary me-2"
              onClick={handleRetryBackend}
            >
              Retry Backend
            </button>
          )}
          <button
            type="button"
            className="btn-close"
            onClick={() => setIsVisible(false)}
          ></button>
        </div>
      </div>
    </div>
  );
}
