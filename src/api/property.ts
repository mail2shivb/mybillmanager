import client from './client';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import type { Property } from '../types';
import { mockAPI } from './mockData';

// Check if we should use mock data (when API is not available)
const useMockData = import.meta.env.VITE_USE_MOCK_DATA === 'true' || 
                    import.meta.env.VITE_API_BASE === undefined;

export const useProperties = () =>
  useQuery({
    queryKey: ['properties'],
    queryFn: async () => {
      if (useMockData) {
        console.log('Using mock data for properties');
        return await mockAPI.getProperties();
      }
      return (await client.get('/properties')).data as Property[];
    },
  });

export const useProperty = (id?: string) =>
  useQuery({
    queryKey: ['properties', id],
    enabled: !!id,
    queryFn: async () => {
      if (!id) throw new Error('No ID provided');
      if (useMockData) {
        console.log('Using mock data for property', id);
        return await mockAPI.getProperty(id);
      }
      return (await client.get(`/properties/${id}`)).data as Property;
    },
  });

export const useCreateProperty = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: async (p: Property) => {
      if (useMockData) {
        console.log('Using mock data to create property', p);
        return await mockAPI.createProperty(p);
      }
      return (await client.post('/properties', p)).data;
    },
    onSuccess: () => qc.invalidateQueries({ queryKey: ['properties'] }),
  });
};

export const useUpdateProperty = (id: number) => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: async (p: Property) => {
      if (useMockData) {
        console.log('Using mock data to update property', id, p);
        return await mockAPI.updateProperty(id, p);
      }
      return (await client.put(`/properties/${id}`, p)).data;
    },
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['properties'] });
      qc.invalidateQueries({ queryKey: ['properties', String(id)] });
    },
  });
};

export const useDeleteProperty = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: async (id: number) => {
      if (useMockData) {
        console.log('Using mock data to delete property', id);
        return await mockAPI.deleteProperty(id);
      }
      return (await client.delete(`/properties/${id}`)).data;
    },
    onSuccess: () => qc.invalidateQueries({ queryKey: ['properties'] }),
  });
};
