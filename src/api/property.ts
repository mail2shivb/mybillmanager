import client from './client';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import type { Property } from '../types';
import { mockAPI } from './mockData';
import { apiCall } from './apiHandler';

export const useProperties = () =>
  useQuery({
    queryKey: ['properties'],
    queryFn: async () => {
      return await apiCall(
        async () => (await client.get('/properties')).data as Property[],
        async () => await mockAPI.getProperties(),
        'get properties'
      );
    },
  });

export const useProperty = (id?: string) =>
  useQuery({
    queryKey: ['properties', id],
    enabled: !!id,
    queryFn: async () => {
      if (!id) throw new Error('No ID provided');
      return await apiCall(
        async () => (await client.get(`/properties/${id}`)).data as Property,
        async () => await mockAPI.getProperty(id),
        `get property ${id}`
      );
    },
  });

export const useCreateProperty = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: async (p: Property) => {
      return await apiCall(
        async () => (await client.post('/properties', p)).data,
        async () => await mockAPI.createProperty(p),
        'create property'
      );
    },
    onSuccess: () => qc.invalidateQueries({ queryKey: ['properties'] }),
  });
};

export const useUpdateProperty = (id: number) => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: async (p: Property) => {
      return await apiCall(
        async () => (await client.put(`/properties/${id}`, p)).data,
        async () => await mockAPI.updateProperty(id, p),
        `update property ${id}`
      );
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
      return await apiCall(
        async () => (await client.delete(`/properties/${id}`)).data,
        async () => await mockAPI.deleteProperty(id),
        `delete property ${id}`
      );
    },
    onSuccess: () => qc.invalidateQueries({ queryKey: ['properties'] }),
  });
};
