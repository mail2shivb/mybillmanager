import client from './client'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { Property } from '../types'

export const useProperties = () => useQuery({ queryKey:['properties'], queryFn: async()=> (await client.get('/properties')).data as Property[] })
export const useProperty = (id?: string) => useQuery({ queryKey:['properties',id], enabled:!!id, queryFn: async()=> (await client.get(`/properties/${id}`)).data as Property })
export const useCreateProperty = () => { const qc=useQueryClient(); return useMutation({ mutationFn: async(p:Property)=> (await client.post('/properties',p)).data, onSuccess:()=> qc.invalidateQueries({queryKey:['properties']}) }) }
export const useUpdateProperty = (id:number) => { const qc=useQueryClient(); return useMutation({ mutationFn: async(p:Property)=> (await client.put(`/properties/${id}`,p)).data, onSuccess:()=>{ qc.invalidateQueries({queryKey:['properties']}); qc.invalidateQueries({queryKey:['properties',String(id)]}) } }) }
export const useDeleteProperty = () => { const qc=useQueryClient(); return useMutation({ mutationFn: async(id:number)=> (await client.delete(`/properties/${id}`)).data, onSuccess:()=> qc.invalidateQueries({queryKey:['properties']}) }) }
