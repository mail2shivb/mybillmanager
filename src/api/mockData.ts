import type { Property } from '../types';

// Mock data for demonstration purposes
export const mockProperties: Property[] = [
  {
    id: 1,
    name: 'Sunset Villa',
    propType: 'Home',
    address: '123 Sunset Boulevard, Los Angeles, CA',
  },
  {
    id: 2,
    name: 'Downtown Apartment',
    propType: 'Home',
    address: '456 Main Street, New York, NY',
  },
  {
    id: 3,
    name: 'Vacant Land Plot',
    propType: 'Land',
    address: '789 Country Road, Austin, TX',
  },
];

// Mock API functions for demonstration
export const mockAPI = {
  getProperties: () => Promise.resolve(mockProperties),
  getProperty: (id: string) => {
    const property = mockProperties.find(p => p.id === Number(id));
    if (property) {
      return Promise.resolve(property);
    }
    return Promise.reject(new Error('Property not found'));
  },
  createProperty: (property: Property) => {
    const newProperty = { ...property, id: Date.now() };
    mockProperties.push(newProperty);
    return Promise.resolve(newProperty);
  },
  updateProperty: (id: number, property: Property) => {
    const index = mockProperties.findIndex(p => p.id === id);
    if (index !== -1) {
      mockProperties[index] = { ...property, id };
      return Promise.resolve(mockProperties[index]);
    }
    return Promise.reject(new Error('Property not found'));
  },
  deleteProperty: (id: number) => {
    const index = mockProperties.findIndex(p => p.id === id);
    if (index !== -1) {
      mockProperties.splice(index, 1);
      return Promise.resolve({ success: true });
    }
    return Promise.reject(new Error('Property not found'));
  },
};
