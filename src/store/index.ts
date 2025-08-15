import { configureStore, createSlice } from '@reduxjs/toolkit';
const uiSlice = createSlice({
  name: 'ui',
  initialState: { message: '' },
  reducers: {
    setMessage: (s, a) => {
      s.message = a.payload || '';
    },
  },
});
export const { setMessage } = uiSlice.actions;
export const store = configureStore({ reducer: { ui: uiSlice.reducer } });
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
